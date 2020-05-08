package com.example.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 字符串工具类
 * @PackagePath com.example.common.utils.StringUtils
 * @Author YINZHIYU
 * @Date 2020/5/8 13:50
 * @Version 1.0.0.0
 **/
public class StringUtils {

    /*
     * @Description 首字母变小写
     * @Params ==>
     * @Param str
     * @Return java.lang.String
     * @Date 2020/4/21 10:05
     * @Auther YINZHIYU
     */
    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] += ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /*
     * @Description 首字母变大写
     * @Params ==>
     * @Param str
     * @Return java.lang.String
     * @Date 2020/4/21 10:05
     * @Auther YINZHIYU
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /*
     * @Description 字符串为 null 或者为  "" 时返回 true
     * @Params ==>
     * @Param str
     * @Return boolean
     * @Date 2020/4/21 10:06
     * @Auther YINZHIYU
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim());
    }

    /*
     * @Description 字符串不为 null 而且不为  ""/"null" 时返回 true
     * @Params ==>
     * @Param str
     * @Return boolean
     * @Date 2020/4/21 10:06
     * @Auther YINZHIYU
     */
    public static boolean notBlank(String str) {
        return str != null && !"".equals(str.trim()) && !"null".equalsIgnoreCase(str.trim());
    }

    public static boolean notBlank(String... strings) {
        if (strings == null) {
            return false;
        }
        for (String str : strings) {
            if (str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim())) {
                return false;
            }
        }
        return true;
    }

    public static boolean notNull(Object... paras) {
        if (paras == null) {
            return false;
        }
        for (Object obj : paras) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将数字转化成设定位数的字符串，不足位数则在数字前补0 <br/>
     * 例如获取6位的字符串：2018 ——> "002018"
     *
     * @param intValue 传入的正整数
     * @param n        几位的字符串
     * @return
     */
    public static String genNumberStr(int intValue, int n) {
        String rs = "";
        if (intValue > 0 && n > 0) {
            rs = String.valueOf(intValue);
            if (n > rs.length()) {
                int addn = n - rs.length();
                for (int i = 0; i < addn; i++) {
                    rs = "0" + rs;
                }
            }
        }
        return rs;
    }

    /**
     * 将数字转化成设定位数的字符串，不足位数则在数字前补0 <br/>
     * 例如获取6位的字符串：2018 ——> "002018"
     *
     * @param intValue 传入的正整数
     * @param n        几位的字符串
     * @return
     */
    public static String genNumberStrByL(Long intValue, int n) {
        String rs = "";
        if (intValue > 0 && n > 0) {
            rs = String.valueOf(intValue);
            if (n > rs.length()) {
                int addn = n - rs.length();
                for (int i = 0; i < addn; i++) {
                    rs = "0" + rs;
                }
            }
        }
        return rs;
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isOnlyNumber(String str) {
        String regEx = "^[0-9]*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isNumber(String str) {
        //采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
        //可以判断正负、整数小数
        String regexRuleInt = "^-?[1-9]\\d*$";
        Pattern patternInt = Pattern.compile(regexRuleInt);
        boolean isInt = patternInt.matcher(str).find();
        String regexRuleDouble = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
        Pattern patternDouble = Pattern.compile(regexRuleDouble);
        boolean isDouble = patternDouble.matcher(str).find();

        return isInt || isDouble;
    }

    /**
     * 获取当前时间字符串：1855555735109
     *
     * @return
     */
    public static String autoGenItemCode() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int milliSecond = now.get(Calendar.MILLISECOND);
        long timeMills = now.getTimeInMillis();
        System.out.println("年: " + year);
        System.out.println("月: " + month);
        System.out.println("日: " + day);
        System.out.println("时: " + hour);
        System.out.println("分: " + minute);
        System.out.println("秒: " + second);
        System.out.println("毫秒: " + milliSecond);
        System.out.println("当前时间毫秒数：" + timeMills);
        String dateStr = year + "";
        dateStr = dateStr.substring(2);

        if (month + 1 < 10) {
            dateStr = dateStr + ("0" + month);
        } else {
            dateStr = dateStr + (month + 1);
        }
        if (day < 10) {
            dateStr = dateStr + ("0" + day);
        } else {
            dateStr = dateStr + day;
        }
        if (hour < 10) {
            dateStr = dateStr + ("0" + hour);
        } else {
            dateStr = dateStr + hour;
        }
        if (minute < 10) {
            dateStr = dateStr + ("0" + minute);
        } else {
            dateStr = dateStr + minute;
        }
        if (milliSecond < 10) {
            dateStr = dateStr + ("00" + milliSecond);
        } else if (milliSecond < 100) {
            dateStr = dateStr + ("0" + milliSecond);
        } else {
            dateStr = dateStr + milliSecond;
        }
//        System.out.println(dateStr);
//        System.out.println(DateUtils.getCurrentTime2());
        return dateStr;
    }

    /**
     * 拼接字符串
     *
     * @param sb
     * @param prefixMsg
     * @param suffixMsg
     * @return prefixMsg：suffixMsg  例如   姓名:啊啊啊; 年龄:18
     */
    public static StringBuffer splicingMsg(StringBuffer sb, String prefixMsg, String suffixMsg) {
        if (StringUtils.isBlank(prefixMsg) || StringUtils.isBlank(suffixMsg)) {
            return sb;
        }
        if (sb.length() > 0) {
            sb.append("; ");
        }
        sb.append(prefixMsg).append("：").append(suffixMsg);
        return sb;
    }


    /**
     * 去除字符串中间的多余的空格，只保留一个空格  例子：  'aaa	 bbb  c '  =>  'aaa bbb c '
     *
     * @param string
     * @return
     */
    public static String removeMiddleBlank(String string) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            String str = "" + string.charAt(i);
//			System.out.println(str+str.length());
            str = str.replaceAll("\\s*", "");
//			System.out.println(str+str.length());
//			System.out.println("----------------------");
            if (str.length() != 0) {
                sb.append(str);
            }
            try {
                String str_next = "" + string.charAt(i + 1);
                str_next = str_next.replaceAll("\\s*", "");
                if (str.length() == 0 && str_next.length() != 0) {
                    sb.append(' ');
                }
            } catch (Exception e) {
                if (str.length() == 0) {
                    sb.append(' ');
                }
            }
        }
        return sb.toString();
    }


    public static StringBuffer appendStrMsg2Sb(StringBuffer sb, String firstMsg, boolean addFirstMsg, String msg, String lastMsg) {
        if (sb == null) {
            return null;
        }
        if (addFirstMsg) {
            sb.append(firstMsg);
        }
        sb.append(msg);
        sb.append(lastMsg);
        return sb;
    }

    /**
     * 字符串按长度拆分成数组
     *
     * @param string
     * @param len
     * @return
     */
    public static String[] splitString(String string, int len) {

        String[] stringArrays = new String[0];

        if (isBlank(string)) {
            return stringArrays;
        }

        int strLen = string.length();

        int splitCount = strLen / len;

        if (splitCount > 0) {
            if ((strLen % len) > 0) {
                stringArrays = new String[splitCount + 1];
            } else {
                stringArrays = new String[splitCount];
            }

            int cache = len;

            int start = 0;
            for (int i = 0; i < stringArrays.length; i++) {
                if (i == 0) {
                    stringArrays[0] = string.substring(start, len);
                    start = len;
                } else if (i > 0 && i < stringArrays.length - 1) {
                    len = len + cache;
                    stringArrays[i] = string.substring(start, len);
                    start = len;
                } else {
                    stringArrays[i] = string.substring(len);
                }
            }
        } else {
            stringArrays = new String[]{string};
        }
        return stringArrays;
    }

    /**
     * 编码
     *
     * @param str
     * @return
     */
    public static String base64Encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解码
     *
     * @param str
     * @return
     */
    public static String base64Decode(String str) {
        return new String(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static void main(String[] args) {


        String taskData = base64Encode("我是一只小小小小鸟");
        System.out.println("编码后 " + taskData);
        System.out.println("字符串长度 " + taskData.length());
        String[] taskDataArray = StringUtils.splitString(taskData, 2);
        System.out.println("数组长度 " + taskDataArray.length);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < taskDataArray.length; i++) {
            sb.append(taskDataArray[i]);
        }
        System.out.println("解码后 " + base64Decode(sb.toString()));
        System.out.println(base64Decode(sb.toString()).substring(1, 8));
        String ex = "1234567";
        String str = ex.length() > 8 ? ex.substring(0, 8) : ex;
        System.out.println(str);

    }

}




