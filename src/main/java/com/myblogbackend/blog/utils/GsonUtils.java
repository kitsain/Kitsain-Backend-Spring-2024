package com.myblogbackend.blog.utils;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class GsonUtils {
    public static <T> List<T> stringToArray(final String s, final Class<T[]> clazz) {
        try {
            T[] arr = new Gson().fromJson(s, clazz);
            return Arrays.asList(arr);
        } catch (Exception e) {
            T res = (T) s;
            return List.of(res);
        }
    }

    public static <T> T stringToObject(final String s, final Class<T> clazz) {
        return new Gson().fromJson(s, clazz);
    }

    public static <T> String arrayToString(final List<T> list) {
        return new Gson().toJson(list);
    }

    public static <T> String objectToString(final T s) {
        if (s == null) {
            return null;
        }
        return new Gson().toJson(s);
    }
}
