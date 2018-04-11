package com.flamemaster.platform.infra.microservice.common.infrastruct.utils;

public class StringUtils {

    public static String cleanToNumberOrLetterString(String src) {
        if(src == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(char c : src.trim().toCharArray()) {
            if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
