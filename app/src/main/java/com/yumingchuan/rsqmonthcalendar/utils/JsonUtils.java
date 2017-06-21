package com.yumingchuan.rsqmonthcalendar.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Gson 简单操作的工具类,,,,注意细节的是Javabean一定保证不能混淆，否则就会异常。踩了一下午的坑，晕死
 *
 * @author Liqi
 */
public class JsonUtils {

    private static final String TAG = "JsonUtils";
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private JsonUtils() {
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * 将对象转换成json格式
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        String jsonStr = null;
        try {
            if (gson != null) {
                jsonStr = gson.toJson(object);
            }
        } catch (Exception e) {
            Log.e(TAG, "object to json string error >>" + e.getMessage());
        }
        return jsonStr;
    }

    /**
     * 将json格式转换成list对象，注意细节的是Javabean一定保证不能混淆，否则就会异常。踩了一下午的坑，晕死
     *
     * @param s
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {

        List<T> list = null;
        try {
            if (gson != null) {
                list = Arrays.asList(gson.fromJson(s, clazz));
                list = new ArrayList<>(list);
            }
        } catch (Exception e) {
            Log.e(TAG, "json string to list<?> error >>" + e.getMessage());
        }

//        if (list == null) {
//            list = new ArrayList<>();
//        }

        return list;
    }


    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) {
        Map<?, ?> map = null;
        try {
            if (gson != null) {
                Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
                }.getType();
                map = gson.fromJson(jsonStr, type);
            }
            return map;
        } catch (Exception e) {
            Log.e(TAG, "json string to map error >>" + e.getMessage());
        }
        return map;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    public static <T> T jsonToBean(String jsonStr, Class<T> cl) {
        T t = null;
        try {
            if (gson != null) {
                t = gson.fromJson(jsonStr, cl);
            }
            return t;
        } catch (Exception e) {
            Log.e(TAG, "json string to bean object error >>" + jsonStr);
        }
        return t;
    }


    /**
     * 获取返回值中某一个值是否为空
     *
     * @param tempStr
     * @param tempParam
     * @return
     */
    public static boolean jsonToBoolean(String tempStr, String tempParam) {
        try {
            return new JSONObject(tempStr).optBoolean(tempParam, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取返回值中某一个值是否为空
     *
     * @param tempStr
     * @param tempParam
     * @return
     */
    public static String jsonToString(String tempStr, String tempParam) {
        String tempString = "";
        try {
            tempString = new JSONObject(tempStr).optString(tempParam, "");
            if ("null".equals(tempString)){
                tempString = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempString;
    }


    /**
     * 获取返回值中某一个值是否为空
     *
     * @param tempStr
     * @param tempParam
     * @return
     */
    public static int jsonToInteger(String tempStr, String tempParam) {
        int tempInt = 0;
        try {
            tempInt = new JSONObject(tempStr).optInt(tempParam, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempInt;
    }


}
