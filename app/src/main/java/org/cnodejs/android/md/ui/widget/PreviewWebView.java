package org.cnodejs.android.md.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

public class PreviewWebView extends ContentWebView {

    private static final String LIGHT_THEME_CSS = "" +
            "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + "file:///android_asset/css/preview_light.css" + "\">\n" +
            "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + "file:///android_asset/css/markdown_light.css" + "\">\n";

    private static final String DARK_THEME_CSS = "" +
            "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + "file:///android_asset/css/preview_dark.css" + "\">\n" +
            "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + "file:///android_asset/css/markdown_dark.css" + "\">\n";

    public PreviewWebView(@NonNull Context context) {
        super(context);
    }

    public PreviewWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreviewWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @NonNull
    @Override
    protected String getThemeCssHtmlSlice(boolean isDarkTheme) {
        return isDarkTheme ? DARK_THEME_CSS : LIGHT_THEME_CSS;
    }

}
