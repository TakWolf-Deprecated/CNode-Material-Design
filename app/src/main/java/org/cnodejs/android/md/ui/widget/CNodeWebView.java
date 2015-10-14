package org.cnodejs.android.md.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import org.cnodejs.android.md.ui.listener.CNodeWebViewClient;

public class CNodeWebView extends WebView {

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

    public void loadRenderedContent(String data) {
        loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

}
