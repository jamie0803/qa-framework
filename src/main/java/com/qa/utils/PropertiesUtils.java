package com.qa.utils;

import com.qa.base.Constants;

import java.io.*;
import java.util.Properties;

/**
 * @Author: Zhang Huijuan
 * @Date: 2022/4/12
 */
public class PropertiesUtils {
    public static Properties properties;
    public static InputStreamReader isr;

    public PropertiesUtils(String profile) {

        try {
            isr = new InputStreamReader(new FileInputStream(profile), "utf-8");
            //is = PropertiesUtils.class.getClassLoader().getResource();
            properties.load(isr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
            }
        }
    }
    //通过指定的key来获取配置文件中对应的value，支持使用默认值
    public static String getConfigValue(String key, String defaultVal) {
        String value = null;
        try {
            value = properties.getProperty(key);
            if (value == null && "".equals(value)) {
                value = defaultVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            value = defaultVal;
        }
        return value;
    }
}
