package com.mf.library.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作
 */
public class StringUtils {

    public static final String XING_4 = "****";
    public static final String XING_10 = "**********";
    /**
     * 大写数字
     */
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    /**
     * 整数部分的单位
     */
    private static final String[] IUNIT = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
    /**
     * 小数部分的单位
     */
    private static final String[] DUNIT = {"角", "分", "厘"};


    /**
     * 判断字符串是否为null或长度为0或者都为空格
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0 || "null".equalsIgnoreCase(str)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        return TextUtils.equals(a, b);
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Encode a string
     *
     * @param str
     * @return
     */
    public static String encodeString(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * Decode a string
     *
     * @param str
     * @return
     */
    public static String decodeString(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 字符串小写转化
     *
     * @param s
     * @return
     */
    public static String toLowerCase(String s) {
        return s.toLowerCase(Locale.getDefault());
    }

    /**
     * 字符串大写转化
     *
     * @param s
     * @return
     */
    public static String toUpperCase(String s) {
        return s.toUpperCase(Locale.getDefault());
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        if (s.isEmpty()) {
            return s;
        }
        int len = s.length();
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /***
     * 给手机号加星
     * 前三后四显示数字
     * @param phoneNum
     * @return
     */
    public static String encryptPhoneNum(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum)) {
            if (phoneNum.length() == 11) {
                String start = phoneNum.substring(0, 3);
                String end = phoneNum.substring(phoneNum.length() - 4, phoneNum.length());
                StringBuffer buffer = new StringBuffer();
                buffer.append(start).append(XING_4).append(end);
                return buffer.toString();
            }
        }
        return "";
    }

    /**
     * 格式化手机号码
     *
     * @param phoneNum
     * @return
     */
    public static String formatPhoneNum(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum)) {
            StringBuffer buffer = new StringBuffer();
            int index = 0;
            buffer.append(phoneNum);
            while (index < phoneNum.length() - 1) {
                if (index == 3 || index == 8) {
                    buffer.insert(index, ' ');
                }
                index++;
            }

            String str = buffer.toString();
            return str;
        }
        return "";
    }

    /***
     * 给身份证加星
     * 前四后四显示数字
     * @param idno
     * @return
     */
    public static String encryptIdNo(String idno) {
        if (!TextUtils.isEmpty(idno)) {
            if (idno.length() == 18) {
                String start = idno.substring(0, 4);
                String end = idno.substring(idno.length() - 4, idno.length());
                StringBuffer buffer = new StringBuffer();
                buffer.append(start).append(XING_10).append(end);
                return buffer.toString();
            }
        }
        return "";
    }

    /**
     * @param in 字符串
     * @return byte[]
     * @brief 将String转换成byte[]
     */
    public static byte[] string2Bytes(String in) {
        try {
            return in.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * byte数组转string
     *
     * @param in
     * @return
     */
    public static String bytes2String(byte[] in) {

        if (in == null || in.length == 0) {
            return "";
        }

        return new String(in);
    }

    /**
     * 字符串 千位符  保留两位小数点后两位
     *
     * @param num
     * @return
     */
    public static String num2thousand00(String num) {
        String numStr = "";
        if (isEmpty(num)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }

    /**
     * 字符串 保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String thousand002num(String num) {
        String numStr = "";
        if (isEmpty(num)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("##0.00");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }

    /**
     * @param phone 是否是手机号
     * @return
     */

    public static boolean isPhone(String phone) {

        if (phone.length() != 11 && !phone.startsWith("1")) {
            return false;
        } else {
            return true;
        }
//        String regex = "^((16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(phone);
//        return m.matches();
    }

    public static boolean isID(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        boolean matches = IDNumber.matches(regularExpression);
        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return matches;
    }

    /**
     * @param password 是否是密码
     * @return
     */
    public static boolean isPassword(String password) {
        String regex = "^(?=.*[A-Za-z0-9])(?=.*\\d{2,})(?=.*[A-Za-z]{2,})[A-Za-z\\d]{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean isMatch = m.matches();
        return isMatch;
    }

    /**
     * 登录名校验
     */

    public static boolean isLoginname(String name) {
        String regex = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        boolean isMatch = m.matches();
        return isMatch;
    }

    /**
     * 账号校验
     */

    public static boolean isBankID(String bankID) {
        String regex = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(bankID);
        boolean isMatch = m.matches();
        return isMatch;
    }


    /**
     *   * 得到大写金额。
     *  
     */
    public static String toChinese(String str) {
        str = str.replaceAll(",", "");// 去掉","
        String integerStr;// 整数部分数字
        String decimalStr;// 小数部分数字

// 初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }
// integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
        if (!integerStr.equals("")) {
            integerStr = Long.toString(Long.parseLong(integerStr));
            if (integerStr.equals("0")) {
                integerStr = "";
            }
        }
        // overflow超出处理能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            System.out.println(str + ":超出处理能力");
            return str;
        }

        int[] integers = toArray(integerStr);// 整数部分数字
        boolean isMust5 = isMust5(integerStr);// 设置万单位
        int[] decimals = toArray(decimalStr);// 小数部分数字
        return getChineseInteger(integers, isMust5) + getChineseDecimal(decimals);
    }

    /**
     *   * 整数部分和小数部分转换为数组，从高位至低位
     *  
     */
    private static int[] toArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    /**
     *   * 得到中文金额的整数部分。
     *  
     */
    private static String getChineseInteger(int[] integers, boolean isMust5) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        for (int i = 0; i < length; i++) {
// 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
// 特殊情况：10(拾元、壹拾元、壹拾万元、拾万元)
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13)// 万(亿)(必填)
                    key = IUNIT[4];
                else if ((length - i) == 9)// 亿(必填)
                    key = IUNIT[8];
                else if ((length - i) == 5 && isMust5)// 万(不必填)
                    key = IUNIT[4];
                else if ((length - i) == 1)// 元(必填)
                    key = IUNIT[0];
// 0遇非0时补零，不包含最后一位
                if ((length - i) > 1 && integers[i + 1] != 0)
                    key += NUMBERS[0];
            }
            chineseInteger.append(integers[i] == 0 ? key
                    : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }

    /**
     *   * 得到中文金额的小数部分。
     *  
     */
    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for (int i = 0; i < decimals.length; i++) {
// 舍去3位小数之后的
            if (i == 3)
                break;
            chineseDecimal.append(decimals[i] == 0 ? ""
                    : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }


    /**
     *   * 判断第5位数字的单位"万"是否应加。
     *  
     */
    private static boolean isMust5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                // 取得从低位数，第5到第8位的字串
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

    /**
     * 截取两个指定字符串中间的字符串
     *  
     */
    public static String splitData(String str, String strStart, String strEnd) {
        String tempStr;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        return tempStr;
    }

    /**
     * 截取以某个字符开头的字符串
     *  
     */
    public static String splitHeaderData(String str, String strStart) {
        String tempStr;
        String str1 = str.substring(0, str.lastIndexOf(strStart));
        tempStr = str.substring(str1.length() + 7, str.length());
        return tempStr;
    }

}