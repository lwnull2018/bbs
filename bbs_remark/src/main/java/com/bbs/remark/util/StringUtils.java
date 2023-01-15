package com.bbs.remark.util;

import cn.hutool.core.util.ObjectUtil;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {


    public static final String genTempFileName(String prefix, Long accountId) {
        if (null == prefix || null == accountId || accountId < 1) prefix = "";
        if ("null".equalsIgnoreCase(prefix)) prefix = "";
        String osPath = Constant.OS_TEMP_DIR;
        if (!osPath.endsWith(File.separator)) {
            osPath = osPath + File.separator;
        }
        String tempFileName = osPath + prefix + "_" + accountId + ".tmp";
        return tempFileName;
    }

    public static final String toString(Object obj) {
        if (ObjectUtil.isNull(obj)) {
            return EMPTY;
        }
        return obj.toString();
    }

    /**
     * 生成缓存的的键
     *
     * @param header
     * @param parts
     * @return
     */
    public static final String cacheKeyGenerator(final String header, final String... parts) {
        if (StringUtils.isEmpty(header)) {
            throw new RuntimeException("key header can not be empty");
        }
        if (null == parts || parts.length <= 0) {
            return header;
        }
        StringBuilder sb = new StringBuilder(header);
        if (!StringUtils.endsWith(header, ":")) {
            sb.append(":");
        }
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean checkMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 手机号验证
     *
     * @param obj
     * @return
     */
    public static boolean checkMobile(final Object obj) {
        if (null == obj) {
            return false;
        }
        return checkMobile(obj.toString());
    }


    /**
     * 验证邮箱
     *
     * @param obj
     * @return
     */
    public static boolean checkEmail(final Object obj) {
        if (null == obj) {
            return false;
        }
        return checkEmail(obj.toString());
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (StringUtils.isEmpty(email) || email.length() > 150) {
            return false;
        }
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 确定字符串是否为数字
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
     * 判断是否为中文字符串
     *
     * @param str
     * @return
     */
    public static boolean isChinese(final String str) {
        String regEx = "^[\\u4e00-\\u9fa5]*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * 清除所有空格跟换行符
     *
     * @param str
     * @return
     */
    public static String cleanBlank(String str) {
        return str.replaceAll(" +", "").replaceAll("\n", "").replaceAll("\r", "");
    }

    public static boolean isJson(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        str = str.trim();
        if ((str.startsWith("{") && str.endsWith("}")) ||
                (str.startsWith("[{") && str.endsWith("}]"))) {
            return true;
        }
        return false;
    }

    /**
     * 是否数字、字母或者二者的组合
     *
     * @param input
     * @return
     */
    public static boolean isNormalizeString(String input) {
        if (isEmpty(input)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]*$");
        return pattern.matcher(input).matches();
    }

    public static String getDomainName(String fullUrl) {
        if (StringUtils.isEmpty(fullUrl)) {
            return "";
        }
        fullUrl = fullUrl.toLowerCase().replace("https://", "")
                .replace("http://", "");
        int end = fullUrl.indexOf("/");
        if (end > 0) {
            fullUrl = fullUrl.substring(0, end);
        }
        return fullUrl;
    }


    // Function to validate the IPs address.
    public static boolean isValidIPAddress(String ip) {

        if (isEmpty(ip)) {
            return false;
        }
        // Regex for digit from 0 to 255.
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        // Regex for a digit from 0 to 255 and
        // followed by a dot, repeat 4 times.
        // this is the regex to validate an IP address.
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the IP address is empty
        // return false
        if (ip == null) {
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given IP address
        // and regular expression.
        Matcher m = p.matcher(ip);
        // Return if the IP address
        // matched the ReGex
        return m.matches();
    }
}
