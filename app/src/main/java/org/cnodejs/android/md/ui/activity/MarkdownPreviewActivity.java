package org.cnodejs.android.md.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.widget.CNodeWebView;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MarkdownPreviewActivity extends BaseActivity {

    private static final String KEY_MARKDOWN = "markdown";

    public static void open(Context context, String markdown) {
        Intent intent = new Intent(context, MarkdownPreviewActivity.class);
        intent.putExtra(KEY_MARKDOWN, markdown);
        context.startActivity(intent);
    }

    @Bind(R.id.markdown_preview_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.markdown_preview_web_view)
    protected CNodeWebView cnodeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        cnodeWebView.loadRenderedContent(FormatUtils.renderMarkdown(getIntent().getStringExtra(KEY_MARKDOWN)));
    }

    @Override
    public void onBackPressed() {
        if (cnodeWebView.canGoBack()) {
            cnodeWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
