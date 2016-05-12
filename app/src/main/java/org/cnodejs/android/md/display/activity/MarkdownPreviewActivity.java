package org.cnodejs.android.md.display.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.util.ThemeUtils;
import org.cnodejs.android.md.display.widget.ContentWebView;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkdownPreviewActivity extends StatusBarActivity {

    private static final String EXTRA_MARKDOWN = "markdown";

    public static void start(@NonNull Activity activity, String markdown) {
        Intent intent = new Intent(activity, MarkdownPreviewActivity.class);
        intent.putExtra(EXTRA_MARKDOWN, markdown);
        activity.startActivity(intent);
    }

    @BindView(R.id.markdown_preview_toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.markdown_preview_web_content)
    protected ContentWebView webContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        webContent.loadRenderedContent(FormatUtils.renderMarkdown(getIntent().getStringExtra(EXTRA_MARKDOWN)));
    }

    @Override
    public void onBackPressed() {
        if (webContent.canGoBack()) {
            webContent.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
