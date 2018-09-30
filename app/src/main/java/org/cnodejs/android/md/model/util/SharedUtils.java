package org.cnodejs.android.md.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.cnodejs.android.md.util.DigestUtils;

import java.lang.reflect.Type;

public final class SharedUtils {

    private SharedUtils() {}

    private static final String TAG = "SharedUtils";

    @NonNull
    public static SharedPreferencesWrapper with(@NonNull Context context, @NonNull String name) {
        return new SharedPreferencesWrapper(context.getSharedPreferences(DigestUtils.SHA256.getHex(name), Context.MODE_PRIVATE));
    }

    public static class SharedPreferencesWrapper {

        private final SharedPreferences sharedPreferences;

        private SharedPreferencesWrapper(@NonNull SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        @NonNull
        public SharedPreferences getSharedPreferences() {
            return sharedPreferences;
        }

        @NonNull
        public SharedPreferencesWrapper clear() {
            sharedPreferences.edit().clear().apply();
            return this;
        }

        public String getString(@NonNull String key, @Nullable String defValue) {
            try {
                return sharedPreferences.getString(key, defValue);
            } catch (ClassCastException e) {
                Log.e(TAG, "Get string value error.", e);
                return defValue;
            }
        }

        @NonNull
        public SharedPreferencesWrapper setString(@NonNull String key, @Nullable String value) {
            sharedPreferences.edit().putString(key, value).apply();
            return this;
        }

        public boolean getBoolean(@NonNull String key, boolean defValue) {
            try {
                return sharedPreferences.getBoolean(key, defValue);
            } catch (ClassCastException e) {
                Log.e(TAG, "Get boolean value error.", e);
                return defValue;
            }
        }

        @NonNull
        public SharedPreferencesWrapper setBoolean(@NonNull String key, boolean value) {
            sharedPreferences.edit().putBoolean(key, value).apply();
            return this;
        }

        public float getFloat(@NonNull String key, float defValue) {
            try {
                return sharedPreferences.getFloat(key, defValue);
            } catch (ClassCastException e) {
                Log.e(TAG, "Get float value error.", e);
                return defValue;
            }
        }

        @NonNull
        public SharedPreferencesWrapper setFloat(@NonNull String key, float value) {
            sharedPreferences.edit().putFloat(key, value).apply();
            return this;
        }

        public int getInt(@NonNull String key, int defValue) {
            try {
                return sharedPreferences.getInt(key, defValue);
            } catch (ClassCastException e) {
                Log.e(TAG, "Get int value error.", e);
                return defValue;
            }
        }

        @NonNull
        public SharedPreferencesWrapper setInt(@NonNull String key, int value) {
            sharedPreferences.edit().putInt(key, value).apply();
            return this;
        }

        public long getLong(@NonNull String key, long defValue) {
            try {
                return sharedPreferences.getLong(key, defValue);
            } catch (ClassCastException e) {
                Log.e(TAG, "Get long value error.", e);
                return defValue;
            }
        }

        @NonNull
        public SharedPreferencesWrapper setLong(@NonNull String key, long value) {
            sharedPreferences.edit().putLong(key, value).apply();
            return this;
        }

        public <T> T getObject(@NonNull String key, @NonNull Type typeOfT) {
            String value = getString(key, null);
            if (value == null) {
                return null;
            } else {
                try {
                    return EntityUtils.gson.fromJson(value, typeOfT);
                } catch (JsonParseException e) {
                    Log.e(TAG, "Get object value error.", e);
                    return null;
                }
            }
        }

        @NonNull
        public SharedPreferencesWrapper setObject(@NonNull String key, @Nullable Object value) {
            setString(key, value == null ? null : EntityUtils.gson.toJson(value));
            return this;
        }

    }

}
