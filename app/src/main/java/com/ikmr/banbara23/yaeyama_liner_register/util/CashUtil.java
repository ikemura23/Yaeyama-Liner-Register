package com.ikmr.banbara23.yaeyama_liner_register.util;

import com.google.gson.Gson;

public class CashUtil {
    /**
     * 前回のキャッシュと値を比較
     *
     * @param json 今回取得した値
     * @param key  前回値が保存されているキー
     * @return true:値が同じ false:違う
     */
    public static boolean isEqualForLastTime(String json, String key) {
        String lastTimeString = PreferenceUtils.get(key, "");
        return lastTimeString.equals(json);
    }

    public static boolean isEqualForLastTime(Object object, String key) {
        String json = new Gson().toJson(object);
        String lastTimeString = PreferenceUtils.get(key, "");
        return lastTimeString.equals(json);
    }

    public static void saveToPref(Object object, String key) {
        try {
            String json = new Gson().toJson(object);
            PreferenceUtils.put(key, json);
        } catch (Exception ignored) {

        }
    }
}
