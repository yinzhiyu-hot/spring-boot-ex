package com.example;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.common.annotations.XmlElementAnno;
import com.example.common.constants.BeanXmlConstants;
import com.example.common.constants.EncodeConstants;
import com.example.common.utils.DateUtils;
import com.example.common.utils.XmlUtil;
import com.example.service.business.SyncTaskService;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @Description 测试用例
 * @PackagePath com.example.ComExampleAppApplicationTests
 * @Author YINZHIYU
 * @Date 2020/5/8 13:45
 * @Version 1.0.0.0
 **/
@SpringBootTest
class ComExampleAppApplicationTests {

    @Resource
    private SyncTaskService syncTaskService;

    private final static SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 取得本地时间：
        Calendar localCal = Calendar.getInstance();

        //取得指定时区的时间：　　　　
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        Calendar usCal = Calendar.getInstance(Locale.US);

        format.setTimeZone(zone);

        System.out.println("------------------------------> utc = " + format.format(localCal.getTime()));
        System.out.println("------------------------------> utc = " + format.format(cal.getTime()));
        System.out.println("------------------------------> utc = " + format.format(usCal.getTime()));
        System.out.println("------------------------------> utc = " + DateUtils.localToUTC(usCal.getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        System.out.println("------------------------------> utcAAA = " + DateUtils.localToUTC("2020-04-15 05:00:00"));
        System.out.println("------------------------------> utc = " + DateUtils.getUTCDateStr());
        System.out.println("------------------------------> cal = " + DateUtils.getUTCTimeStrByTimeZone(zone));
        System.out.println("------------------------------> usCal = " + DateUtils.getUTCTimeStrByLocale(Locale.US));
        System.out.println("------------------------------> usCal = " + DateUtils.getUTCTimeStrByLocale(Locale.CHINA));


        //取美国时间对应UTC时间
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date usDate = format2.parse(DateUtils.getUTCTimeStrByLocale(Locale.US));
            System.out.println(" -------------------------------------- >" + format2.format(usDate));
            Date usDate2 = DateUtil.offsetDay(usDate, -1);
            System.out.println(" -------------------------------------- >" + format2.format(usDate2));
            Date usDate3 = DateUtil.offsetDay(usDate, 0);
            System.out.println(" -------------------------------------- >" + format2.format(usDate3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThread() {
        handSynOrders(new ArrayList<String>() {{
            this.add("US");
            this.add("FR");
        }});
    }

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    public void handSynOrders(List<String> sites) {
        try {
            CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(sites.size());

            startTime.set(System.currentTimeMillis());

            sites.forEach(r -> {
                ThreadUtil.execAsync(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Site -> " + r);
                        try {
                            Thread.sleep(RandomUtil.randomInt(1, 6) * 1000);
                        } catch (Exception e) {
                        }
                        countDownLatch.countDown();
                    }
                });
            });

            countDownLatch.await();
            System.out.println(String.format("本次报告下载完成, 耗时: %s ms", (System.currentTimeMillis() - startTime.get())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTime() throws UnsupportedEncodingException {
        String startDate = DateUtils.getUTCTimeStrByLocaleOffset(Locale.US, -1);

        String endDate = DateUtils.getUTCTimeStrByLocaleOffset(Locale.US, 0);

//        LocalDateTime localDateTime = LocalDateTime.parse(String.format("%sT00:00:00.001Z",startDate));
//
//        ZonedDateTime zdt = localDateTime.atZone(ZoneOffset.UTC); //you might use a different zone
//        String iso8601 = zdt.toString();

        String iso8601 = String.format("%sT23:59:59.999Z", startDate);
        ZonedDateTime zdt = ZonedDateTime.parse(iso8601);
        LocalDateTime ldt = zdt.toLocalDateTime();

        zdt = ldt.atZone(ZoneOffset.UTC); //you might use a different zone
        iso8601 = zdt.toString();

        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(iso8601);

        String creationDateAfter = String.format("%sT00:00:00.001Z", startDate);

        String filter = String.format("creationdate:[%s..]", creationDateAfter);
        System.out.println("-----------------> " + filter);
    }


    /**
     * xml转javaBean
     */
    @Test
    public void parseXmlToJaBean() throws Exception {
        //组装xml
        StringBuilder sb = new StringBuilder();
        sb.append("<root>");
        sb.append("<name>wangkecheng</name>");
        sb.append("<address>上海</address>");
        sb.append("<age>27</age>");
        sb.append("</root>");

        JaBeanToXml jaBeanToXml = (JaBeanToXml) XmlUtil.parseXmlToBean(sb.toString(), JaBeanToXml.class, BeanXmlConstants.FIELD_ANNOTATION_NAME);
        System.out.println(jaBeanToXml.toString());
    }

    /**
     * bean转xml
     */
    @Test
    public void parseJaBeanToXml() throws Exception {

        JaBeanToXml jaBeanToXml = new JaBeanToXml();
        jaBeanToXml.setName("wangkecheng");
        jaBeanToXml.setAddress("上海");
        //jaBeanToXml.setAge(28);

        String xml = XmlUtil.parseBeanToXml(jaBeanToXml, JaBeanToXml.class, true, EncodeConstants.ENCODE_UTF8);
        System.out.println(xml);
        JaBeanToXml jaBeanToXml2 = (JaBeanToXml) XmlUtil.parseXmlToBean(xml, JaBeanToXml.class, BeanXmlConstants.FIELD_NAME);
        JaBeanToXml jaBeanToXml3 = XmlUtil.parseXmlToBean(xml, JaBeanToXml.class);

        //System.out.println(jaBeanToXml2.toString());
        System.out.println(jaBeanToXml3.toString());
    }

    @XmlRootElement(name = "root")
    public static class JaBeanToXml {

        @XmlElement(name = "name")
        @XmlElementAnno
        private String name;

        @XmlElement(name = "address")
        @XmlElementAnno
        private String address;

        @XmlElementAnno
        //@XmlElement(name = "age")
        private int age;

        @XmlTransient
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlTransient
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @XmlTransient
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "JaBeanToXml{" +
                    "name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    public void testUtc() {
        System.out.println(DateUtils.localToUTC(new Date(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
    }


}
