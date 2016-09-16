package org.cnodejs.android.md.ui.viewholder;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.widget.ListView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadMoreFooter {

    public static final int STATE_DISABLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_ENDLESS = 3;
    public static final int STATE_FAILED = 4;

    @IntDef({STATE_DISABLE, STATE_LOADING, STATE_FINISHED, STATE_ENDLESS, STATE_FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    @BindView(R.id.icon_loading)
    protected View iconLoading;

    @BindView(R.id.tv_text)
    protected TextView tvText;

    @State
    private int state = STATE_DISABLE;

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
        if (getState() == STATE_ENDLESS || getState() == STATE_FAILED) {
            setState(STATE_LOADING);
            loadMoreListener.onLoadMore();
        }
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(@State int state) {
        if (this.state != state) {
            this.state = state;
            switch (state) {
                case STATE_DISABLE:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.GONE);
                    tvText.setClickable(false);
                    break;
                case STATE_LOADING:
                    iconLoading.setVisibility(View.VISIBLE);
                    tvText.setVisibility(View.GONE);
                    tvText.setClickable(false);
                    break;
                case STATE_FINISHED:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_nomore);
                    tvText.setClickable(false);
                    break;
                case STATE_ENDLESS:
                    iconLoading.setVisibility(View.GONE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_endless);
                    tvText.setClickable(true);
                    break;
                case STATE_FAILED:
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
