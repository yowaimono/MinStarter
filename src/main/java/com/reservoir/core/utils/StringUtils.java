package com.reservoir.core.utils;

//import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class StringUtils {

    /**
     * 判断字符串是否为空或null
     *
     * @param str 输入字符串
     * @return 如果字符串为空或null，返回true；否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空且不为null
     *
     * @param str 输入字符串
     * @return 如果字符串不为空且不为null，返回true；否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 去除字符串两端的空白字符
     *
     * @param str 输入字符串
     * @return 去除两端空白字符后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 将字符串转换为大写
     *
     * @param str 输入字符串
     * @return 转换为大写后的字符串
     */
    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }


    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param camelCaseString 驼峰命名的字符串
     * @return 下划线命名的字符串
     */
    public static String Camelconvert(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return camelCaseString;
        }

        StringBuilder snakeCaseString = new StringBuilder();
        char[] chars = camelCaseString.toCharArray();

        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                snakeCaseString.append("_").append(Character.toLowerCase(c));
            } else {
                snakeCaseString.append(c);
            }
        }

        // 如果转换后的字符串以 "_" 开头，则去掉开头的 "_"
        if (snakeCaseString.charAt(0) == '_') {
            snakeCaseString.deleteCharAt(0);
        }

        return snakeCaseString.toString();
    }
    /**
     * 将字符串转换为小写
     *
     * @param str 输入字符串
     * @return 转换为小写后的字符串
     */
    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * 判断字符串是否以指定前缀开头
     *
     * @param str    输入字符串
     * @param prefix 前缀
     * @return 如果字符串以指定前缀开头，返回true；否则返回false
     */
    public static boolean startsWith(String str, String prefix) {
        return str != null && prefix != null && str.startsWith(prefix);
    }

    /**
     * 判断字符串是否以指定后缀结尾
     *
     * @param str    输入字符串
     * @param suffix 后缀
     * @return 如果字符串以指定后缀结尾，返回true；否则返回false
     */
    public static boolean endsWith(String str, String suffix) {
        return str != null && suffix != null && str.endsWith(suffix);
    }

    /**
     * 替换字符串中的指定字符
     *
     * @param str        输入字符串
     * @param oldChar    要替换的字符
     * @param newChar    新字符
     * @return 替换后的字符串
     */
    public static String replace(String str, char oldChar, char newChar) {
        return str == null ? null : str.replace(oldChar, newChar);
    }

    /**
     * 替换字符串中的指定子字符串
     *
     * @param str       输入字符串
     * @param target    要替换的子字符串
     * @param replacement 新子字符串
     * @return 替换后的字符串
     */
    public static String replace(String str, String target, String replacement) {
        return str == null ? null : str.replace(target, replacement);
    }

    /**
     * 将字符串按指定分隔符分割成字符串数组
     *
     * @param str       输入字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串数组
     */
    public static String[] split(String str, String delimiter) {
        return str == null ? null : str.split(delimiter);
    }

    /**
     * 将字符串按指定分隔符分割成字符串列表
     *
     * @param str       输入字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串列表
     */
    public static List<String> splitToList(String str, String delimiter) {
        return str == null ? null : Arrays.asList(str.split(delimiter));
    }

    /**
     * 将字符串列表拼接成一个字符串，使用指定的分隔符
     *
     * @param list      字符串列表
     * @param delimiter 分隔符
     * @return 拼接后的字符串
     */
    public static String join(List<String> list, String delimiter) {
        return list == null ? null : String.join(delimiter, list);
    }

    /**
     * 反转字符串
     *
     * @param str 输入字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        return str == null ? null : new StringBuilder(str).reverse().toString();
    }

    /**
     * 判断字符串是否包含指定子字符串
     *
     * @param str    输入字符串
     * @param subStr 子字符串
     * @return 如果包含子字符串，返回true；否则返回false
     */
    public static boolean contains(String str, String subStr) {
        return str != null && subStr != null && str.contains(subStr);
    }

    /**
     * 去除字符串中的所有空白字符
     *
     * @param str 输入字符串
     * @return 去除所有空白字符后的字符串
     */
    public static String removeWhitespace(String str) {
        return str == null ? null : str.replaceAll("\\s+", "");
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str 输入字符串
     * @return 如果字符串为数字，返回true；否则返回false
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }

    /**
     * 将字符串的首字母大写
     *
     * @param str 输入字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 将字符串的首字母小写
     *
     * @param str 输入字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 将字符串中的每个单词的首字母大写
     *
     * @param str 输入字符串
     * @return 每个单词首字母大写后的字符串
     */
    public static String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Arrays.stream(str.split("\\s+"))
                .map(word -> word.isEmpty() ? word : word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * 判断字符串是否为空或仅包含空白字符
     *
     * @param str 输入字符串
     * @return 如果字符串为空或仅包含空白字符，返回true；否则返回false
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空且不包含仅空白字符
     *
     * @param str 输入字符串
     * @return 如果字符串不为空且不包含仅空白字符，返回true；否则返回false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 将字符串中的多个连续空白字符替换为一个空格
     *
     * @param str 输入字符串
     * @return 替换后的字符串
     */
    public static String normalizeWhitespace(String str) {
        return str == null ? null : str.replaceAll("\\s+", " ");
    }

    /**
     * 将字符串中的HTML标签移除
     *
     * @param str 输入字符串
     * @return 移除HTML标签后的字符串
     */
    public static String removeHtmlTags(String str) {
        return str == null ? null : str.replaceAll("<[^>]*>", "");
    }

    /**
     * 将字符串中的特殊字符进行HTML转义
     *
     * @param str 输入字符串
     * @return 转义后的字符串
     */
    public static String escapeHtml(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder();
        for (char c : str.toCharArray()) {
            switch (c) {
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '&':
                    escaped.append("&amp;");
                    break;
                case '"':
                    escaped.append("&quot;");
                    break;
                case '\'':
                    escaped.append("&#39;");
                    break;
                default:
                    escaped.append(c);
            }
        }
        return escaped.toString();
    }

    /**
     * 将字符串中的换行符替换为指定字符串
     *
     * @param str       输入字符串
     * @param replacement 替换换行符的字符串
     * @return 替换后的字符串
     */
    public static String replaceNewLines(String str, String replacement) {
        return str == null ? null : str.replaceAll("\\r?\\n", replacement);
    }

    /**
     * 将字符串中的所有空格替换为指定字符串
     *
     * @param str       输入字符串
     * @param replacement 替换空格的字符串
     * @return 替换后的字符串
     */
    public static String replaceAllSpaces(String str, String replacement) {
        return str == null ? null : str.replaceAll("\\s", replacement);
    }

    /**
     * 将字符串中的所有字母转换为大写
     *
     * @param str 输入字符串
     * @return 所有字母转换为大写后的字符串
     */
    public static String toUpperCaseAllLetters(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * 将字符串中的所有字母转换为小写
     *
     * @param str 输入字符串
     * @return 所有字母转换为小写后的字符串
     */
    public static String toLowerCaseAllLetters(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * 将字符串中的所有字母转换为首字母大写，其余小写
     *
     * @param str 输入字符串
     * @return 所有字母转换为首字母大写，其余小写后的字符串
     */
    public static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Arrays.stream(str.split("\\s+"))
                .map(word -> word.isEmpty() ? word : word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    /**
     * 判断字符串是否匹配指定的正则表达式
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @return 如果字符串匹配正则表达式，返回true；否则返回false
     */
    public static boolean matches(String str, String regex) {
        if (str == null || regex == null) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 查找字符串中第一个匹配正则表达式的子字符串
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @return 第一个匹配的子字符串，如果没有匹配的子字符串，返回null
     */
    public static String findFirstMatch(String str, String regex) {
        if (str == null || regex == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 查找字符串中所有匹配正则表达式的子字符串
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @return 所有匹配的子字符串列表，如果没有匹配的子字符串，返回空列表
     */
    public static List<String> findAllMatches(String str, String regex) {
        if (str == null || regex == null) {
            return Collections.emptyList();
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 替换字符串中第一个匹配正则表达式的子字符串
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @param replacement 替换字符串
     * @return 替换后的字符串
     */
    public static String replaceFirst(String str, String regex, String replacement) {
        if (str == null || regex == null || replacement == null) {
            return str;
        }
        return str.replaceFirst(regex, replacement);
    }

    /**
     * 替换字符串中所有匹配正则表达式的子字符串
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @param replacement 替换字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(String str, String regex, String replacement) {
        if (str == null || regex == null || replacement == null) {
            return str;
        }
        return str.replaceAll(regex, replacement);
    }

    /**
     * 分割字符串，使用正则表达式作为分隔符
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @return 分割后的字符串数组
     */
    public static String[] splitByRegex(String str, String regex) {
        if (str == null || regex == null) {
            return new String[0];
        }
        return str.split(regex);
    }

    /**
     * 将字符串中指定范围内的字符用星号(*)替代
     *
     * @param str   输入字符串
     * @param start 起始位置（包含）
     * @param end   结束位置（不包含）
     * @return 替代后的字符串
     */
    public static String maskString(String str, int start, int end) {
        if (str == null || start < 0 || end > str.length() || start >= end) {
            return str;
        }
        StringBuilder masked = new StringBuilder(str);
        for (int i = start; i < end; i++) {
            masked.setCharAt(i, '*');
        }
        return masked.toString();
    }


    public static String maskString(String str, int start, int end,char a) {
        if (str == null || start < 0 || end > str.length() || start >= end) {
            return str;
        }
        StringBuilder masked = new StringBuilder(str);
        for (int i = start; i < end; i++) {
            masked.setCharAt(i, a);
        }
        return masked.toString();
    }

    /**
     * 分割字符串，使用正则表达式作为分隔符，并返回列表
     *
     * @param str       输入字符串
     * @param regex     正则表达式
     * @return 分割后的字符串列表
     */
    public static List<String> splitToListByRegex(String str, String regex) {
        if (str == null || regex == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(regex));
    }

//    /**
//     * 判断字符串是否包含敏感词
//     *
//     * @param text 待验证的字符串
//     * @return 布尔值
//     */
//    public static boolean containsSensitiveWord(String text) {
//        return SensitiveWordHelper.contains(text);
//    }
//
//    /**
//     * 返回字符串中第一个敏感词
//     *
//     * @param text 待验证的字符串
//     * @return 字符串
//     */
//    public static String findFirstSensitiveWord(String text) {
//        return SensitiveWordHelper.findFirst(text);
//    }
//
//    /**
//     * 返回字符串中所有敏感词
//     *
//     * @param text 待验证的字符串
//     * @return 字符串列表
//     */
//    public static List<String> findAllSensitiveWords(String text) {
//        return SensitiveWordHelper.findAll(text);
//    }
//
//    /**
//     * 使用默认的替换策略替换敏感词
//     *
//     * @param text 待处理的字符串
//     * @return 处理后的字符串
//     */
//    public static String replaceSensitiveWords(String text) {
//        return SensitiveWordHelper.replace(text);
//    }
//
//    /**
//     * 使用指定的 char 替换敏感词
//     *
//     * @param text 待处理的字符串
//     * @param replacement 替换字符
//     * @return 处理后的字符串
//     */
//    public static String replaceSensitiveWords(String text, char replacement) {
//        return SensitiveWordHelper.replace(text, replacement);
//    }

    public static boolean hasLength(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }


}