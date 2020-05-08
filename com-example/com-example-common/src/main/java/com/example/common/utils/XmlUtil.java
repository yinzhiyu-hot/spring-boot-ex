package com.example.common.utils;

import com.example.common.annotations.XmlElementAnno;
import com.example.common.constants.BeanXmlConstants;
import com.example.common.constants.EncodeConstants;
import org.apache.commons.beanutils.BeanUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description xml工具类[采用JDOM解析, 对SAX的封装]
 * @PackagePath com.example.common.utils.XmlUtil
 * @Author YINZHIYU
 * @Date 2020/5/8 13:50
 * @Version 1.0.0.0
 **/
public class XmlUtil {

    /*
     * @Description 将javaBean转换为xml对象[要想使用该方法进行xml转换，class 上必须有XmlRootElement注解]
     * @Params ==>
     * @Param bean 转换的实体类
     * @Param clazz 转换的实体类模板
     * @Param fragment 是否创建头信息[true 创建]
     * @Param encoding 头信息中的编码格式encoding设置
     * @Return java.lang.String
     * @Date 2020/5/6 15:03
     * @Auther YINZHIYU
     */
    public static String parseBeanToXml(Object bean, Class clazz, boolean fragment, String encoding) throws Exception {
        StringWriter sw = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//xml格式化  该值默认为false，true 格式化
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, !fragment);//创建头信息 该值默认为false，true 不创建[<?xml version="1.0" encoding="UTF-8" standalone="yes"?>]
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);// 设置头信息中的编码格式
            jaxbMarshaller.marshal(bean, sw);
        } catch (JAXBException e) {
            throw new Exception(e);
        }
        return sw.toString();
    }

    /*
     * @Description 将xml对象转换为javaBean
     * @Params ==>
     * @Param xml
     * @Param c
     * @Return T
     * @Date 2020/5/6 15:05
     * @Auther YINZHIYU
     */
    public static <T> T parseXmlToBean(String xml, Class<T> c) throws Exception {
        T t;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw new Exception(e);
        }
        return t;
    }

    /*
     * @Description  将xml对象转换为javaBean [此处可能有其它常用的非自定义类型对象的参与转换，但并没做对应的解析，待以后添加]
     * @Params ==>
     * @Param xml 转换的xml
     * @Param clazz 转换的实体类
     * @Param matchType xml中取节点的匹配规则  1. FIELD_NAME javabean 字段名  2. FIELD_ANNOTATION_NAME 注解里配置的
     * @Return java.lang.Object
     * @Date 2020/5/6 15:10
     * @Auther YINZHIYU
     */
    public static Object parseXmlToBean(String xml, Class clazz, String matchType) throws Exception {
        if (StringUtils.notBlank(xml)) {
            Field[] fields = clazz.getDeclaredFields();//转换对象定义的字段
            List<Field> fieldList = new ArrayList<Field>();//转换对象定义的字段对应定义的xml节点名称

            for (Field fie : fields) {
                if (BeanXmlConstants.FIELD_ANNOTATION_NAME.equals(matchType)) {
                    if (fie.isAnnotationPresent(XmlElementAnno.class)) {//只解析配置了XmlElementAnno的属性[特殊玩家]
                        fieldList.add(fie);
                    }
                }
                if (BeanXmlConstants.FIELD_NAME.equals(matchType)) {
                    fieldList.add(fie);//普通玩家
                }
            }
            try {
                Document doc = createDocument(xml);
                Element root = doc.getRootElement(); //取的根节点
                Object object = clazz.newInstance(); //实例化需要接收转换值的bean
                if (!fieldList.isEmpty()) {
                    for (Field field : fieldList) {
                        if (field.getType().isPrimitive() || field.getType().getName().startsWith("java.lang")) {//基本类型
                            Element child = null;
                            if (BeanXmlConstants.FIELD_NAME.equals(matchType)) {
                                child = root.getChild(field.getName());//根据javabean 属性名称找xml节点
                            }
                            if (BeanXmlConstants.FIELD_ANNOTATION_NAME.equals(matchType)) {
                                child = root.getChild(field.getDeclaredAnnotation(XmlElement.class).name());//根据javabean 属性配置的注解名称找xml节点
                            }
                            if (child != null) {
                                BeanUtils.setProperty(object, field.getName(), child.getValue());//将获取到的节点的值，通过反射设置到 对应的属性名称上
                            }
                        } else if (field.getType().isAssignableFrom(List.class)) {//集合
                            List<Element> childList = null;
                            if (BeanXmlConstants.FIELD_NAME.equals(matchType)) {
                                childList = root.getChildren(field.getName());//根据javabean 属性名称找xml节点集合
                            }
                            if (BeanXmlConstants.FIELD_ANNOTATION_NAME.equals(matchType)) {
                                childList = root.getChildren(field.getDeclaredAnnotation(XmlElement.class).name());//根据javabean 属性配置的注解名称找xml节点集合
                            }
                            if (childList != null) {

                                List<Object> objects = new ArrayList<>();

                                Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
                                Class genericClazz = null;//用户设置值的类对象
                                if (fc instanceof ParameterizedType) { // 如果是泛型参数的类型
                                    ParameterizedType pt = (ParameterizedType) fc;
                                    genericClazz = (Class) pt.getActualTypeArguments()[0]; //得到泛型里的class类型对象。
                                }

                                for (Element subElement : childList) {//遍历该节点集合下的每个节点
                                    StringBuilder subXml = new StringBuilder();//拼接节点集合下每个节点的xml报文，包含当前节点
                                    subXml.append(StringFormatter.format("<{0}>", subElement.getName()));
                                    for (Element subElementTemp : subElement.getChildren()) {
                                        subXml.append(StringFormatter.format("<{0}>{1}</{2}>", subElementTemp.getName(), subElementTemp.getValue(), subElementTemp.getName()));
                                    }
                                    subXml.append(StringFormatter.format("</{0}>", subElement.getName()));
                                    Object transObject = parseXmlToBean(subXml.toString(), genericClazz, matchType);//将每个节点对应的xml报文，转成List中对应的泛型对象
                                    objects.add(transObject);
                                }

                                BeanUtils.setProperty(object, field.getName(), objects);//将获取到的节点集合的值，通过反射设置到 对应的集合属性名称上
                            }
                        } else {//对象，获取这个节点下的xml报文
                            Element child = null;
                            if (BeanXmlConstants.FIELD_NAME.equals(matchType)) {//根据javabean 属性名称找xml节点
                                child = root.getChild(field.getName());
                            }
                            if (BeanXmlConstants.FIELD_ANNOTATION_NAME.equals(matchType)) {
                                child = root.getChild(field.getDeclaredAnnotation(XmlElement.class).name());//根据javabean 属性配置的注解名称找xml节点
                            }
                            if (child != null) {
                                Object subObject = field.getType().newInstance();
                                Object transObject = parseXmlToBean(apendElement(child), subObject.getClass(), matchType);
                                BeanUtils.setProperty(object, field.getName(), transObject);
                            }
                        }
                    }
                }
                return object;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        return null;
    }

    /*
     * @Description 递归获取某个节点下的所有xml内容，包含当前节点
     * @Params ==>
     * @Param currentElement
     * @Return java.lang.String
     * @Date 2020/5/6 15:06
     * @Auther YINZHIYU
     */
    public static String apendElement(Element currentElement) {
        StringBuilder subXml = new StringBuilder();
        if (currentElement.getChildren() != null && currentElement.getChildren().size() > 0) {
            subXml.append(StringFormatter.format("<{0}>", currentElement.getName()));
            for (Element childElement : currentElement.getChildren()) {
                subXml.append(apendElement(childElement));
            }
            subXml.append(StringFormatter.format("</{0}>", currentElement.getName()));
        } else {
            subXml.append(StringFormatter.format("<{0}>{1}</{2}>", currentElement.getName(), currentElement.getValue(), currentElement.getName()));
        }
        return subXml.toString();
    }

    /*
     * @Description 根据xml 获取文档
     * @Params ==>
     * @Param xml
     * @Return org.jdom2.Document
     * @Date 2020/5/6 15:06
     * @Auther YINZHIYU
     */
    public static Document createDocument(String xml) throws Exception {
        StringReader read = new StringReader(xml);
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        return sb.build(source);//创建xml的文档
    }

    /*
     * @Description 格式化输出xml
     * @Params ==>
     * @Param xml
     * @Return java.lang.String
     * @Date 2020/5/6 15:06
     * @Auther YINZHIYU
     */
    public static String formatXml(String xml) throws Exception {
        Document doc = createDocument(xml);

        //格式化输出xml文件字符串
        Format format = Format.getCompactFormat();
        format.setEncoding(EncodeConstants.ENCODE_UTF8);
        //这行保证输出后的xml的格式
        format.setIndent("    ");
        XMLOutputter xmlout = new XMLOutputter(format);
        ByteArrayOutputStream byteRsp = new ByteArrayOutputStream();
        xmlout.output(doc, byteRsp);

        return byteRsp.toString(EncodeConstants.ENCODE_UTF8);
    }

}