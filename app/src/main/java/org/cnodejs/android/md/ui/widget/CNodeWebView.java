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

import java.util.ArrayList;
import java.util.List;

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
    private List<OnScrollListener> onScrollListenerList;

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

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListenerList != null && onScrollListenerList.size() > 0) {
            for (OnScrollListener onScrollListener : onScrollListenerList) {
                onScrollListener.onScrollChanged(l, t, oldl, oldt);
            }
        }
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (onScrollListenerList == null) {
            onScrollListenerList = new ArrayList<>();
        }
        onScrollListenerList.add(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        if (onScrollListenerList != null) {
            onScrollListenerList.remove(listener);
        }
    }

    public void clearOnScrollListeners() {
        if (onScrollListenerList != null) {
            onScrollListenerList.clear();
        }
    }

    public interface OnScrollListener {

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }

}
