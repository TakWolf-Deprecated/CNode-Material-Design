package org.cnodejs.android.md.storage;

import android.content.Context;
import android.text.TextUtils;

import org.cnodejs.android.md.util.codec.Digest;

import java.util.UUID;

public final class DeviceInfo {

    private DeviceInfo() {}

    private static final String TAG = "DeviceInfo";
    private static final String KEY_DEVICE_TOKEN = "deviceToken";
    private volatile static String deviceToken = null;

    public static String getDeviceToken(Context context) {
        if (TextUtils.isEmpty(deviceToken)) {
            synchronized (DeviceInfo.class) {
                if (TextUtils.isEmpty(deviceToken)) {
                    deviceToken = context.getSharedPreferences(Digest.MD5.getMessage(TAG), Context.MODE_PRIVATE).getString(KEY_DEVICE_TOKEN, null);
                }
                if (TextUtils.isEmpty(deviceToken)) {
                    deviceToken = Digest.MD5.getMessage(UUID.randomUUID().toString());
                    context.getSharedPreferences(Digest.MD5.getMessage(TAG), Context.MODE_PRIVATE).edit().putString(KEY_DEVICE_TOKEN, deviceToken).apply();
                }
            }
        }
        return deviceToken;
    }

}
