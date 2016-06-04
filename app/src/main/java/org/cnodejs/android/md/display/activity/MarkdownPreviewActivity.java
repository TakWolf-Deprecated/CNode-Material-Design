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
import org.cnodejs.android.md.display.widget.PreviewWebView;
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

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.web_preview)
    protected PreviewWebView webPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        String markdown = getIntent().getStringExtra(EXTRA_MARKDOWN);
        webPreview.loadRenderedContent(FormatUtils.handleHtml(FormatUtils.renderMarkdown(markdown)));
    }

    @Override
    public void onBackPressed() {
        if (webPreview.canGoBack()) {
            webPreview.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
