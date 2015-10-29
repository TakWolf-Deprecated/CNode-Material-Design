package org.cnodejs.android.md.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import org.cnodejs.android.md.storage.SettingShared;
import org.cnodejs.android.md.ui.listener.CNodeWebViewClient;

public class CNodeWebView extends WebView {

    private static final String THEME_CSS_LIGHT = "file:///android_asset/cnode_light.css";
    private static final String THEME_CSS_DARK = "file:///android_asset/cnode_dark.css";

    private static final String HTML_0 = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1\">";

    private static final String HTML_1 = "\n" +
            "</head>\n" +
            "<body>\n";

    private static final String HTML_2 = "\n" +
            "</body>\n" +
            "</html>";

    public CNodeWebView(Context context) {
        super(context);
        setWebViewClient(CNodeWebViewClient.with(context));
    }

    public CNodeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWebViewClient(CNodeWebViewClient.with(context));
    }

    public CNodeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWebViewClient(CNodeWebViewClient.with(context));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CNodeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWebViewClient(CNodeWebViewClient.with(context));
    }

    private String getThemeCSS() {
        return SettingShared.isEnableThemeDark(getContext()) ? THEME_CSS_DARK : THEME_CSS_LIGHT;
    }

    public void loadRenderedContent(String data) {
        data = HTML_0 + "\n<link type=\"text/css\" rel=\"stylesheet\" href=\"" + getThemeCSS() + "\">" + HTML_1 + data + HTML_2;
        loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

}
