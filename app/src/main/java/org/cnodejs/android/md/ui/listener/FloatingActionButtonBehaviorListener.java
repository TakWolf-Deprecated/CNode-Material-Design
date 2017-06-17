package org.cnodejs.android.md.ui.listener;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import org.cnodejs.android.md.ui.widget.CNodeWebView;

public final class FloatingActionButtonBehaviorListener {

    private FloatingActionButtonBehaviorListener() {}

    private static final int SCROLL_THRESHOLD = 4;

    public static class ForRecyclerView extends RecyclerView.OnScrollListener {

        private final FloatingActionButton floatingActionButton;

        public ForRecyclerView(@NonNull FloatingActionButton floatingActionButton) {
            this.floatingActionButton = floatingActionButton;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > SCROLL_THRESHOLD) {
                floatingActionButton.hide();
            } else if (dy < -SCROLL_THRESHOLD) {
                floatingActionButton.show();
            }
        }

    }

    public static class ForListView implements AbsListView.OnScrollListener {

        private final FloatingActionButton floatingActionButton;

        private int lastScrollY;
        private int previousFirstVisibleItem;

        public ForListView(@NonNull FloatingActionButton floatingActionButton) {
            this.floatingActionButton = floatingActionButton;
        }

        @Override
        public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0) {
                if (firstVisibleItem == previousFirstVisibleItem) {
                    int newScrollY = listView.getChildCount() > 0 ? listView.getChildAt(0).getTop() : 0;
                    if (newScrollY - lastScrollY < -SCROLL_THRESHOLD) {
                        floatingActionButton.hide();
                    } else if (newScrollY - lastScrollY > SCROLL_THRESHOLD) {
                        floatingActionButton.show();
                    }
                    lastScrollY = newScrollY;
                } else {
                    if (firstVisibleItem > previousFirstVisibleItem) {
                        floatingActionButton.hide();
                    } else if (firstVisibleItem < previousFirstVisibleItem) {
                        floatingActionButton.show();
                    }
                    lastScrollY = listView.getChildCount() > 0 ? listView.getChildAt(0).getTop() : 0;
                    previousFirstVisibleItem = firstVisibleItem;
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

    }

    public static class ForWebView implements CNodeWebView.OnScrollListener {

        private final FloatingActionButton floatingActionButton;

        public ForWebView(@NonNull FloatingActionButton floatingActionButton) {
            this.floatingActionButton = floatingActionButton;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            if (t - oldt > SCROLL_THRESHOLD) {
                floatingActionButton.hide();
            } else if (t - oldt < -SCROLL_THRESHOLD) {
                floatingActionButton.show();
            }
        }

    }

}
