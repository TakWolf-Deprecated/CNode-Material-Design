package org.cnodejs.android.md.listener;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cnodejs.android.md.activity.UserDetailActivity;
import org.cnodejs.android.md.util.ShipUtils;

public class WebViewContentClient extends WebViewClient {

    private Context context;

    public WebViewContentClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (url.startsWith("https://cnodejs.org/user/")) { // 跳转到用户页面
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("loginName", url.substring(25));
            context.startActivity(intent);
        } else { // 其他连接
            ShipUtils.openUrlByBrowser(context, url);
        }
        return true;
    }

}
