package okhttp3;

import android.support.annotation.NonNull;

public final class OkHttpHackUtils {

    private OkHttpHackUtils() {}

    public static void setRequestHeaderLenient(@NonNull Request.Builder builder, @NonNull String name, @NonNull String value) {
        builder.headers.removeAll(name);
        builder.headers.addLenient(name, value);
    }

}
