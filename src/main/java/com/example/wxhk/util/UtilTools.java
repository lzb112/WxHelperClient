package com.example.wxhk.util;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author: wxlinzebin
 * @create: 2024-03-29
 */
public class UtilTools {
    public static String formatString(String messagePattern, Object ...args){
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, args);
        return formattingTuple.getMessage();
    }
}
