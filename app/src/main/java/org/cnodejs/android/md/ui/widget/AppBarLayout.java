package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppBarLayout extends com.google.android.material.appbar.AppBarLayout {
    public AppBarLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public AppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (appBarLayout.getTotalScrollRange() + verticalOffset < 0) {
                appBarLayout.setExpanded(false);
            }
        });
    }
}
