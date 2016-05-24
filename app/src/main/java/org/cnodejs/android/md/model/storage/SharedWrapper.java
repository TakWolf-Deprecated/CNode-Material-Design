package org.cnodejs.android.md.model.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.cnodejs.android.md.util.codec.DES3;
import org.cnodejs.android.md.util.Digest;
import org.cnodejs.android.md.model.util.EntityUtils;

import java.lang.reflect.Type;

public final class SharedWrapper {

    public static SharedWrapper with(@NonNull Context context, @NonNull String name) {
        return new SharedWrapper(context, name);
    }

    private final Context context;
    private final SharedPreferences sp;

    private SharedWrapper(@NonNull Context context, @NonNull String name) {
        this.context = context.getApplicationContext();
        sp = context.getSharedPreferences(getDigestKey(name), Context.MODE_PRIVATE);
    }

    private String getDigestKey(@NonNull String key) {
        return Digest.MD5.getHex(key);
    }

    private String getSecretKey() {
        return Digest.SHA256.getHex(DeviceInfo.getDeviceToken(context));
    }

    private String get(@NonNull String key, @Nullable String defValue) {
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

    private void set(@NonNull String key, @Nullable String value) {
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(getDigestKey(key), DES3.encrypt(getSecretKey(), TextUtils.isEmpty(value) ? "" : value));
        } catch (Exception e) {
            editor.putString(getDigestKey(key), "");
        }
        editor.apply();
    }

    public String getString(@NonNull String key, @Nullable String defValue) {
        return get(key, defValue);
    }

    public void setString(@NonNull String key, @Nullable String value) {
        set(key, value);
    }

    public boolean getBoolean(@NonNull String key, boolean defValue) {
        return Boolean.parseBoolean(get(key, Boolean.toString(defValue)));
    }

    public void setBoolean(@NonNull String key, boolean value) {
        set(key, Boolean.toString(value));
    }

    public float getFloat(@NonNull String key, float defValue) {
        return Float.parseFloat(get(key, Float.toString(defValue)));
    }

    public void setFloat(@NonNull String key, float value) {
        set(key, Float.toString(value));
    }

    public int getInt(@NonNull String key, int defValue) {
        return Integer.parseInt(get(key, Integer.toString(defValue)));
    }

    public void setInt(@NonNull String key, int value) {
        set(key, Integer.toString(value));
    }

    public long getLong(@NonNull String key, long defValue) {
        return Long.parseLong(get(key, Long.toString(defValue)));
    }

    public void setLong(@NonNull String key, long value) {
        set(key, Long.toString(value));
    }

    public <T>T getObject(@NonNull String key, @NonNull Class<T> clz) {
        String json = get(key, null);
        if (json == null) {
            return null;
        } else {
            try {
                return EntityUtils.gson.fromJson(json, clz);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public <T>T getObject(@NonNull String key, @NonNull Type typeOfT) {
        String json = get(key, null);
        if (json == null) {
            return null;
        } else {
            try {
                return EntityUtils.gson.fromJson(json, typeOfT);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setObject(@NonNull String key, @Nullable Object value) {
        if (value == null) {
            set(key, "");
        } else {
            String json = EntityUtils.gson.toJson(value);
            set(key, json);
        }
    }

    public void clear() {
        sp.edit().clear().apply();
    }

}
