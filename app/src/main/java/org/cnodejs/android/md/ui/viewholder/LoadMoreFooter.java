package org.cnodejs.android.md.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadMoreFooter {

    public enum State {
        disable, loading, finished, endless, failed
    }

    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    @BindView(R.id.icon_loading)
    protected View iconLoading;

    @BindView(R.id.tv_text)
    protected TextView tvText;

    private State state = State.disable;
    private final OnLoadMoreListener loadMoreListener;

    public LoadMoreFooter(@NonNull Context context, @NonNull ListView listView, @NonNull OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        View footerView = LayoutInflater.from(context).inflate(R.layout.footer_load_more, listView, false);
        ButterKnife.bind(this, footerView);
        listView.addFooterView(footerView, null, false);
        listView.addOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    checkLoadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

        });
    }

    private void checkLoadMore() {
        if (getState() == State.endless || getState() == State.failed) {
            setState(State.loading);
            loadMoreListener.onLoadMore();
        }
    }

    @NonNull
    public State getState() {
        return state;
    }

    public void setState(@NonNull State state) {
        if (this.state != state) {
            this.state = state;
            switch (state) {
                case disable:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.GONE);
                    tvText.setClickable(false);
                    break;
                case loading:
                    iconLoading.setVisibility(View.VISIBLE);
                    tvText.setVisibility(View.GONE);
                    tvText.setClickable(false);
                    break;
                case finished:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_nomore);
                    tvText.setClickable(false);
                    break;
                case endless:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_endless);
                    tvText.setClickable(true);
                    break;
                case failed:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_fail);
                    tvText.setClickable(true);
                    break;
                default:
                    throw new AssertionError("Unknow state.");
            }
        }
    }

    @OnClick(R.id.tv_text)
    protected void onBtnTextClick() {
        checkLoadMore();
    }

}
