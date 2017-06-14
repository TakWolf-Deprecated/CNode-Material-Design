package org.cnodejs.android.md.ui.viewholder;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.takwolf.android.hfrecyclerview.HeaderAndFooterRecyclerView;

import org.cnodejs.android.md.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadMoreFooter {

    public static final int STATE_DISABLED = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_ENDLESS = 3;
    public static final int STATE_FAILED = 4;

    @IntDef({STATE_DISABLED, STATE_LOADING, STATE_FINISHED, STATE_ENDLESS, STATE_FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @BindView(R.id.tv_text)
    TextView tvText;

    @State
    private int state = STATE_DISABLED;

    private final OnLoadMoreListener loadMoreListener;

    public LoadMoreFooter(@NonNull Context context, @NonNull HeaderAndFooterRecyclerView recyclerView, @NonNull OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        View footerView = LayoutInflater.from(context).inflate(R.layout.footer_load_more, recyclerView.getFooterContainer(), false);
        recyclerView.addFooterView(footerView);
        ButterKnife.bind(this, footerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    checkLoadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    checkLoadMore();
                }
            }

        });
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(@State int state) {
        if (this.state != state) {
            this.state = state;
            switch (state) {
                case STATE_DISABLED:
                    progressWheel.setVisibility(View.INVISIBLE);
                    progressWheel.stopSpinning();
                    tvText.setVisibility(View.INVISIBLE);
                    tvText.setText(null);
                    tvText.setClickable(false);
                    break;
                case STATE_LOADING:
                    progressWheel.setVisibility(View.VISIBLE);
                    progressWheel.spin();
                    tvText.setVisibility(View.INVISIBLE);
                    tvText.setText(null);
                    tvText.setClickable(false);
                    break;
                case STATE_FINISHED:
                    progressWheel.setVisibility(View.INVISIBLE);
                    progressWheel.stopSpinning();
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_finished);
                    tvText.setClickable(false);
                    break;
                case STATE_ENDLESS:
                    progressWheel.setVisibility(View.INVISIBLE);
                    progressWheel.stopSpinning();
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(null);
                    tvText.setClickable(true);
                    break;
                case STATE_FAILED:
                    progressWheel.setVisibility(View.INVISIBLE);
                    progressWheel.stopSpinning();
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(R.string.load_more_failed);
                    tvText.setClickable(true);
                    break;
                default:
                    throw new AssertionError("Unknow load more state.");
            }
        }
    }

    private void checkLoadMore() {
        if (getState() == STATE_ENDLESS || getState() == STATE_FAILED) {
            setState(STATE_LOADING);
            loadMoreListener.onLoadMore();
        }
    }

    @OnClick(R.id.tv_text)
    void onBtnTextClick() {
        checkLoadMore();
    }

}
