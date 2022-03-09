package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import org.cnodejs.android.md.util.JsonUtils;

public class ContentWebView extends CNodeWebView {
    private static final String HTML_PATH = "file:///android_asset/content.html";

    @Nullable private String content;

    public ContentWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public ContentWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContentWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ContentWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        loadUrl(HTML_PATH);
    }

    private void callJsUpdateContent(@NonNull String content) {
        String json = JsonUtils.INSTANCE.getMoshi().adapter(String.class).toJson(content);
        String script = "updateContent(" + json + ")";
        evaluateJavascript(script, null);
    }

    public void updateContent(@NonNull String content) {
        if (isJavascriptReady()) {
            callJsUpdateContent(content);
        } else {
            this.content = content;
        }
    }

    @Override
    protected void onJavascriptReady() {
        if (content != null) {
            callJsUpdateContent(content);
            content = null;
        }
    }
}
