package org.cnodejs.android.md.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.cnodejs.android.md.ui.jsbridge.AppJavascriptInterface;

import java.util.ArrayList;
import java.util.List;

public class CNodeWebView extends WebView {
    @Nullable private OnHideMaskListener onHideMaskListener;
    private final List<OnScrollChangedListener> onScrollChangedListeners = new ArrayList<>();

    private boolean javascriptReady = false;

    @Nullable private Insets insets;

    public CNodeWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(@NonNull Context context) {
        getSettings().setJavaScriptEnabled(true);

        addJavascriptInterface(new AppJavascriptInterface(), AppJavascriptInterface.NAME);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!javascriptReady) {
                    javascriptReady = true;
                    if (insets != null) {
                        callJsUpdateWebViewInsets(insets);
                        insets = null;
                    }
                    onJavascriptReady();
                    if (onHideMaskListener != null) {
                        onHideMaskListener.OnHideMask();
                    }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(this, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat windowInsets) {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
                if (javascriptReady) {
                    callJsUpdateWebViewInsets(insets);
                } else {
                    CNodeWebView.this.insets = insets;
                }
                return windowInsets;
            }
        });
    }

    public boolean isJavascriptReady() {
        return javascriptReady;
    }

    protected void onJavascriptReady() {}

    private void callJsUpdateWebViewInsets(Insets insets) {
        int webPx = Math.round(insets.bottom / getResources().getDisplayMetrics().density);
        String script = "document.body.style.paddingBottom = '" + webPx + "px'";
        evaluateJavascript(script, null);
    }

    public void setOnHideMaskListener(@Nullable OnHideMaskListener onHideMaskListener) {
        this.onHideMaskListener = onHideMaskListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        for (OnScrollChangedListener listener : onScrollChangedListeners) {
            listener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void addOnScrollChangedListener(@NonNull OnScrollChangedListener listener) {
        onScrollChangedListeners.add(listener);
    }

    public void removeOnScrollChangedListener(@NonNull OnScrollChangedListener listener) {
        onScrollChangedListeners.remove(listener);
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public interface OnHideMaskListener {
        void OnHideMask();
    }
}
