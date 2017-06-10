package org.cnodejs.android.md.ui.util;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import org.cnodejs.android.md.R;

public final class RefreshUtils {

    private RefreshUtils() {}

    public static void init(@NonNull SwipeRefreshLayout refreshLayout, @NonNull SwipeRefreshLayout.OnRefreshListener refreshListener) {
        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(refreshListener);
    }

    public static void refresh(@NonNull final SwipeRefreshLayout refreshLayout, @NonNull final SwipeRefreshLayout.OnRefreshListener refreshListener) {
        refreshLayout.setRefreshing(true);
        refreshListener.onRefresh();
    }

}
