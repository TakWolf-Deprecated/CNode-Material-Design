package org.cnodejs.android.md.ui.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.util.Navigator;

public abstract class CNodeWebView extends WebView {

    private final WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (!TextUtils.isEmpty(url) && !Navigator.openStandardLink(webView.getContext(), url)) {
                Navigator.openInBrowser(webView.getContext(), url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CNodeWebView.this.onPageFinished(url);
        }

    };

    private boolean darkTheme;

    public CNodeWebView(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CNodeWebView, defStyleAttr, defStyleRes);
        darkTheme = a.getBoolean(R.styleable.CNodeWebView_darkTheme, false);
        a.recycle();

        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(webViewClient);
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    protected void onPageFinished(String url) {}

}
