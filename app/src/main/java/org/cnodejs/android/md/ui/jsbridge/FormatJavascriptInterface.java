package org.cnodejs.android.md.ui.jsbridge;

import android.webkit.JavascriptInterface;

import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

public final class FormatJavascriptInterface {

    public static final String NAME = "formatBridge";

    @JavascriptInterface
    public String getRelativeTimeSpanString(String time) {
        return FormatUtils.getRelativeTimeSpanString(new DateTime(time));
    }

}
