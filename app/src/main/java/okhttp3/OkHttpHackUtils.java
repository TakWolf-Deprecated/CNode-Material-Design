package okhttp3;

import android.support.annotation.NonNull;

public final class OkHttpHackUtils {

    private OkHttpHackUtils() {}

    public static void addRequestHeaderLenient(@NonNull Request.Builder builder, @NonNull String name, @NonNull String value) {
        builder.headers.addLenient(name, value);
    }

}
