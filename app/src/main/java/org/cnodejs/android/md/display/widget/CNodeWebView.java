package org.cnodejs.android.md.display.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import org.cnodejs.android.md.display.listener.DefaultWebViewClient;
import org.cnodejs.android.md.display.listener.ImageJavascriptInterface;
import org.cnodejs.android.md.model.storage.SettingShared;

public class CNodeWebView extends WebView {

    private static final String THEME_CSS_LIGHT = "file:///android_asset/cnode_light.css";
    private static final String THEME_CSS_DARK = "file:///android_asset/cnode_dark.css";

    private static final String HTML_0 = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1\">\n";

    private static final String HTML_1 = "" +
            "</head>\n" +
            "<body>\n";

    private static final String HTML_2 = "" +
            "<script>\n" +
            "(function() {\n" +
            "    var objs = document.getElementsByTagName('img');\n" +
            "    for (var i = 0; i < objs.length; i++) {\n" +
            "        objs[i].onclick = function() {\n" +
            "            if (this.parentNode.nodeName !== 'A') {\n" +
            "                window." + ImageJavascriptInterface.NAME + ".openImage(this.src);\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "})();" +
            "</script>\n" +
            "</body>\n" +
            "</html>";

    public CNodeWebView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public CNodeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CNodeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CNodeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(ImageJavascriptInterface.with(context), ImageJavascriptInterface.NAME);
        setWebViewClient(DefaultWebViewClient.instance);
    }

    private String getThemeCSS() {
        return SettingShared.isEnableThemeDark(getContext()) ? THEME_CSS_DARK : THEME_CSS_LIGHT;
    }

    public void loadRenderedContent(String data) {
        data = HTML_0 + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + getThemeCSS() + "\">\n" + HTML_1 + data + "\n" + HTML_2;
        loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

}
