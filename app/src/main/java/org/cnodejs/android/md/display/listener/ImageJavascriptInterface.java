package org.cnodejs.android.md.display.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import org.cnodejs.android.md.display.activity.ImagePreviewActivity;

public class ImageJavascriptInterface {

    private volatile static ImageJavascriptInterface singleton;

    public static ImageJavascriptInterface with(@NonNull Context context) {
        if (singleton == null) {
            synchronized (ImageJavascriptInterface.class) {
                if (singleton == null) {
                    singleton = new ImageJavascriptInterface(context);
                }
            }
        }
        return singleton;
    }

    public static final String NAME = "imageInterface";

    private final Context context;

    private ImageJavascriptInterface(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @JavascriptInterface
    public void openImage(String imageUrl) {
        ImagePreviewActivity.start(context, imageUrl);
    }

}