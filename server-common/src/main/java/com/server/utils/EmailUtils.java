package com.server.utils;

import java.util.regex.Pattern;

/**
 * 邮箱工具累
 */
public class EmailUtils {
    // 基础邮箱验证模式
    private static final String BASIC_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // 严格邮箱验证模式（支持更多特殊字符）
    private static final String STRICT_EMAIL_PATTERN =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+" +
                    "(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * 验证邮箱是否符合基础格式
     * @param email 要验证的邮箱地址
     * @return true 如果邮箱格式有效，false 否则
     */
    public static boolean isValidBasicFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(BASIC_EMAIL_PATTERN, email.trim());
    }

    /**
     * 验证邮箱是否符合严格格式
     * @param email 要验证的邮箱地址
     * @return true 如果邮箱格式有效，false 否则
     */
    public static boolean isValidStrictFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(STRICT_EMAIL_PATTERN, email.trim());
    }

    /**
     * 获取邮箱的本地部分（@符号前面的部分）
     * @param email 邮箱地址
     * @return 本地部分，如果不是有效邮箱返回null
     */
    public static String getLocalPart(String email) {
        if (!isValidBasicFormat(email)) {
            return null;
        }
        return email.split("@")[0];
    }

    /**
     * 获取邮箱的域名部分（@符号后面的部分）
     * @param email 邮箱地址
     * @return 域名部分，如果不是有效邮箱返回null
     */
    public static String getDomain(String email) {
        if (!isValidBasicFormat(email)) {
            return null;
        }
        return email.split("@")[1];
    }

    /**
     * 清理邮箱地址中的非法字符
     * @param email 原始邮箱地址
     * @return 清理后的邮箱地址
     */
    public static String cleanEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        // 移除首尾空白字符
        email = email.trim();

        // 替换多个点为单个点
        email = email.replaceAll("\\.{2,}", ".");

        // 只保留允许的字符
        email = email.replaceAll("[^a-zA-Z0-9._%+-@]", "");

        return email;
    }
}
