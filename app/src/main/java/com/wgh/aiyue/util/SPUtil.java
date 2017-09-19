package com.wgh.aiyue.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wgh.aiyue.MyApplication;

import java.util.Map;

/**
 * Created by   : WGH.
 */
public class SPUtil {

    /**
     * The name of the SP file saved in the phone
     */
    private static final String FILE_NAME = "SPU_WILL_FLOW";

    /**
     * Save data
     */
    public static void put(Context context, String key, Object obj) {
        if (null == context) {
            context = MyApplication.context();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            editor.putString(key, (String) obj);
        }
        editor.apply();
    }

    public static void put(String key, Object object) {
        put(MyApplication.context(), key, object);
    }


    /**
     * Gets the specified data
     */
    public static Object get(Context context, String key, Object defaultObj) {
        if (null == context) {
            context = MyApplication.context();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultObj instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObj);
        } else if (defaultObj instanceof APPTheme) {
            return sharedPreferences.getString(key, ((APPTheme) defaultObj).name());
        }
        return null;
    }

    public static Object get(String key, Object object) {
        return get(MyApplication.context(), key, object);
    }

    /**
     * Delete specified data
     */
    public static void remove(Context context, String key) {
        if (null == context) {
            context = MyApplication.context();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void remove(String key) {
        remove(MyApplication.context(), key);
    }


    /**
     * Returns all key pairs
     */
    public static Map<String, ?> getAll(Context context) {
        if (null == context) {
            context = MyApplication.context();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Map<String, ?> map = sharedPreferences.getAll();
        return map;
    }

    public static Map<String, ?> getAll() {
        return getAll(MyApplication.context());
    }

    /**
     * Delete all data
     */
    public static void clear(Context context) {
        if (null == context) {
            context = MyApplication.context();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void clear() {
        clear(MyApplication.context());
    }

    /**
     * Check if the data corresponding to key exists
     */
    public static boolean contains(Context context, String key) {
        if (null == context) {
            context = MyApplication.context();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    public static boolean contains(String key) {
        return contains(MyApplication.context(), key);
    }
}
