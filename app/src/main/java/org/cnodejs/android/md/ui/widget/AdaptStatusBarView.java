package org.cnodejs.android.md.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import org.cnodejs.android.md.util.ResUtils;

public class AdaptStatusBarView extends View {

    public AdaptStatusBarView(Context context) {
        super(context);
    }

    public AdaptStatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptStatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AdaptStatusBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), ResUtils.getStatusBarHeight(getContext()));
        } else {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), 0);
        }
    }

}
