package org.cnodejs.android.md.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

public class ListView extends android.widget.ListView {

    private final OnScrollListenerProxy scrollListenerProxy = new OnScrollListenerProxy();

    public ListView(@NonNull Context context) {
        super(context);
        init();
    }

    public ListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setOnScrollListener(scrollListenerProxy);
    }

    @Override
    @Deprecated
    public void setOnScrollListener(OnScrollListener listener) {
        scrollListenerProxy.setOnScrollListener(listener);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        scrollListenerProxy.addOnScrollListener(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        scrollListenerProxy.removeOnScrollListener(listener);
    }

    public void clearOnScrollListeners() {
        scrollListenerProxy.clearOnScrollListeners();
    }

    private static class OnScrollListenerProxy implements OnScrollListener {

        private OnScrollListener scrollListener;
        private List<OnScrollListener> scrollListenerList;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollListener != null) {
                scrollListener.onScrollStateChanged(view, scrollState);
            }
            if (scrollListenerList != null && scrollListenerList.size() > 0) {
                for (OnScrollListener onScrollListener : scrollListenerList) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (scrollListener != null) {
                scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (scrollListenerList != null && scrollListenerList.size() > 0) {
                for (OnScrollListener onScrollListener : scrollListenerList) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        }

        public void setOnScrollListener(OnScrollListener listener) {
            scrollListener = listener;
        }

        public void addOnScrollListener(OnScrollListener listener) {
            if (scrollListenerList == null) {
                scrollListenerList = new ArrayList<>();
            }
            scrollListenerList.add(listener);
        }

        public void removeOnScrollListener(OnScrollListener listener) {
            if (scrollListenerList != null) {
                scrollListenerList.remove(listener);
            }
        }

        public void clearOnScrollListeners() {
            if (scrollListenerList != null) {
                scrollListenerList.clear();
            }
        }

    }

}
