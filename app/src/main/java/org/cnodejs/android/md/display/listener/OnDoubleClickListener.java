package org.cnodejs.android.md.display.listener;

import android.view.View;

public abstract class OnDoubleClickListener implements View.OnClickListener {

    private final long delayTime;
    private long lastClickTime = 0;

    public OnDoubleClickListener(long delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public final void onClick(View v) {
        long nowClickTime = System.currentTimeMillis();
        if (nowClickTime - lastClickTime > delayTime) {
            lastClickTime = nowClickTime;
        } else {
            onDoubleClick(v);
        }
    }

    public abstract void onDoubleClick(View v);

}
