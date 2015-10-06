package org.cnodejs.android.md.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.cnodejs.android.md.util.codec.DES3;
import org.cnodejs.android.md.util.codec.Digest;
import org.cnodejs.android.md.util.gson.GsonWrapper;

import java.lang.reflect.Type;

public final class SharedWrapper {

    private volatile static String SECRET_KEY;

    public static SharedWrapper with(Context context, String name) {
        if (TextUtils.isEmpty(SECRET_KEY)) {
            synchronized (SharedWrapper.class) {
                if (TextUtils.isEmpty(SECRET_KEY)) { // 获取设备ID
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    SECRET_KEY = tm.getDeviceId();
                }
                if (TextUtils.isEmpty(SECRET_KEY)) { // 获取AndroidId
                    SECRET_KEY = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
                if (TextUtils.isEmpty(SECRET_KEY)) { // 获取序列号
                    SECRET_KEY = Build.SERIAL;
                }
                if (TextUtils.isEmpty(SECRET_KEY)) { // 获取包名称-最保底的选择
                    SECRET_KEY = context.getPackageName();
                }
            }
        }
        return new SharedWrapper(context, name);
    }

    private SharedPreferences sp;

    private SharedWrapper(Context context, String name) {
        sp = context.getSharedPreferences(getDigestKey(name), Context.MODE_PRIVATE);
    }

    private String getDigestKey(String key) {
        return Digest.SHA256.getMessage(key);
    }

    private String getSecretKey() {
        return Digest.SHA256.getMessage(SECRET_KEY);
    }

    private String get(String key, String defValue) {
        try {
            String value = DES3.decrypt(getSecretKey(), sp.getString(getDigestKey(key), ""));
            if (TextUtils.isEmpty(value)) {
                return defValue;
            } else {
                return value;
            }
        } catch (Exception e) {
            return defValue;
        }
    }

    private void set(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(getDigestKey(key), DES3.encrypt(getSecretKey(), value));
        } catch (Exception e) {
            editor.putString(getDigestKey(key), "");
        }
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return get(key, defValue);
    }

    public void setString(String key, String value) {
        set(key, value);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.parseBoolean(get(key, Boolean.toString(defValue)));
    }

    public void setBoolean(String key, boolean value) {
        set(key, Boolean.toString(value));
    }

    public float getFloat(String key, float defValue) {
        return Float.parseFloat(get(key, Float.toString(defValue)));
    }

    public void setFloat(String key, float value) {
        set(key, Float.toString(value));
    }

    public int getInt(String key, int defValue) {
        return Integer.parseInt(get(key, Integer.toString(defValue)));
    }

    public void setInt(String key, int value) {
        set(key, Integer.toString(value));
    }

    public long getLong(String key, long defValue) {
        return Long.parseLong(get(key, Long.toString(defValue)));
    }

    public void setLong(String key, long value) {
        set(key, Long.toString(value));
    }

    public <T> T getObject(String key, Class<T> clz) {
        String json = get(key, null);
        if (json == null) {
            return null;
        } else {
            try {
                return GsonWrapper.gson.fromJson(json, clz);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public <T> T getObject(String key, Type typeOfT) {
        String json = get(key, null);
        if (json == null) {
            return null;
        } else {
            try {
                return GsonWrapper.gson.fromJson(json, typeOfT);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setObject(String key, Object value) {
        if (value == null) {
            set(key, "");
        } else {
            String json = GsonWrapper.gson.toJson(value);
            set(key, json);
        }
    }

    public void clear() {
        sp.edit().clear().apply();
    }

}
