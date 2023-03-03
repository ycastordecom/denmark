package com.uspsassa.phishing.common.utils;

import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;

public class BaseUtil {
    // Convert a list of objects to a list of objects of the same type
    public static <T> List<T> convertList(List<?> list, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Object o : list) {
            T t = Convert.convert(clazz, o);
            result.add(t);
        }
        return result;
    }
}

