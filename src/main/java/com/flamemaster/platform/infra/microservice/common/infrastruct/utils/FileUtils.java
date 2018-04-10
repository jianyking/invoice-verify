package com.flamemaster.platform.infra.microservice.common.infrastruct.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtils {
    public static String readFileByChars(String fileName, String charsetName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), charsetName == null ? "UTF-8" : charsetName);
            int tempChar;
            StringBuilder sb = new StringBuilder();
            while ((tempChar = reader.read()) != -1) {
                sb.append((char)tempChar);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
