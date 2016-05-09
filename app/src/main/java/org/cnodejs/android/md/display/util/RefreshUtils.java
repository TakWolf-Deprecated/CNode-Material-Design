package org.cnodejs.android.md.display.util;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.util.HandlerUtils;

public final class RefreshUtils {

    private RefreshUtils() {}

    public static void initOnCreate(@NonNull SwipeRefreshLayout refreshLayout, @NonNull SwipeRefreshLayout.OnRefreshListener refreshListener) {
        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(refreshListener);
    }

    /**
     * TODO refreshLayout无法直接在onCreate中设置刷新状态
     */
    public static void refreshOnCreate(@NonNull final SwipeRefreshLayout refreshLayout, @NonNull final SwipeRefreshLayout.OnRefreshListener refreshListener) {
        HandlerUtils.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                refreshListener.onRefresh();
            }

        }, 100);
    }

}
