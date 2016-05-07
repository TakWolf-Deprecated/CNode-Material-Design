package org.cnodejs.android.md.display.listener;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cnodejs.android.md.display.util.Navigator;

public class DefaultWebViewClient extends WebViewClient {

    public static final DefaultWebViewClient instance = new DefaultWebViewClient();

    private DefaultWebViewClient() {}

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Navigator.openLink(webView.getContext(), url);
        return true;
    }

}
