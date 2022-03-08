package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import java.util.ArrayList;
import java.util.List;

public class CNodeWebView extends WebView {
    private final List<OnScrollChangedListener> onScrollChangedListeners = new ArrayList<>();

    public CNodeWebView(@NonNull Context context) {
        super(context);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CNodeWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
}
