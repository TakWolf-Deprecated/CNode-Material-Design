package org.cnodejs.android.md.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.adapter.TopicAdapter;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.storage.LoginShared;
import org.cnodejs.android.md.util.HandlerUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicActivity  extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, TopicAdapter.OnAtClickListener {

    @Bind(R.id.topic_layout_root)
    protected ViewGroup layoutRoot;

    @Bind(R.id.topic_toolbar)
    protected Toolbar toolbar;
    
    @Bind(R.id.topic_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @Bind(R.id.topic_recycler_view)
    protected RecyclerView recyclerView;

    @Bind(R.id.topic_fab_reply)
    protected FloatingActionButton fabReply;

    @Bind(R.id.topic_layout_no_data)
    protected ViewGroup layoutNoData;

    private PopupWindow replyWindow;

    private String topicId;
    private TopicWithReply topic;

    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra("topicId");

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicAdapter(this, this);
        recyclerView.setAdapter(adapter);

        fabReply.attachToRecyclerView(recyclerView);

        // 创建回复窗口
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_reply_window, layoutRoot, false);


        replyWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        replyWindow.setBackgroundDrawable(new ColorDrawable(0x01000000));
        replyWindow.setFocusable(true);
        replyWindow.setOutsideTouchable(true);
        replyWindow.setAnimationStyle(R.style.AppTheme_ReplyWindowAnim);


        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(this);

        HandlerUtils.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }

        }, 100); // refreshLayout无法直接在onCreate中设置刷新状态
    }

    @Override
    public void onRefresh() {
        getTopicAsyncTask(false);
    }

    private void getTopicAsyncTask(boolean gotoBottom) {
        ApiClient.service.getTopic(topicId, false, new Callback<Result<TopicWithReply>>() {

            @Override
            public void success(Result<TopicWithReply> result, Response response) {
                if (!isFinishing()) {
                    topic = result.getData();
                    adapter.setTopic(result.getData());
                    adapter.notifyDataSetChanged();
                    layoutNoData.setVisibility(View.GONE);

                    // TODO 移动到最底部

                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isFinishing()) {
                    Toast.makeText(TopicActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }

        });
    }

    @Override
    public void onAt(String loginName) {



        replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.topic_fab_reply)
    protected void onBtnReplyClick() {
        if (topic != null) {
            if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
                adapter.showNeedLoginDialog();
            } else {
                replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
            }
        }
    }

}
