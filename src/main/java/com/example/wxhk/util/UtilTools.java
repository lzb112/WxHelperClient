package com.example.wxhk.util;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Random;

/**
 * @author: wxlinzebin
 * @create: 2024-03-29
 */
public class UtilTools {
    public static String formatString(String messagePattern, Object ...args){
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, args);
        return formattingTuple.getMessage();
    }

    public static String RandomString(int length) {
        StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
}
