package org.cnodejs.android.md.display.listener;

import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cnodejs.android.md.display.util.Navigator;

public class DefaultWebViewClient extends WebViewClient {

    public static final DefaultWebViewClient instance = new DefaultWebViewClient();

    private DefaultWebViewClient() {}

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (!TextUtils.isEmpty(url) && !Navigator.openStandardLink(webView.getContext(), url)) {
            Navigator.openInBrowser(webView.getContext(), url);
        }
        return true;
    }

}
