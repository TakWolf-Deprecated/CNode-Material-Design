package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import org.cnodejs.android.md.model.entity.TopicDetail;
import org.jetbrains.annotations.NotNull;

public class TopicDetailWebView extends CNodeWebView {
    public TopicDetailWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TopicDetailWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopicDetailWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TopicDetailWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(@NonNull Context context) {
        // TODO
        loadUrl("https://github.com");
    }

    public void updateAccountId(@Nullable String id) {
        // TODO
    }

    public void updateTopicDetail(@NotNull TopicDetail topicDetail) {
        // TODO
    }
}