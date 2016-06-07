package org.cnodejs.android.md.display.listener;

import android.webkit.JavascriptInterface;

import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

public final class FormatJavascriptInterface {

    public static final FormatJavascriptInterface instance = new FormatJavascriptInterface();
    public static final String NAME = "formatBridge";

    private FormatJavascriptInterface() {}

    @JavascriptInterface
    public String getRelativeTimeSpanString(String time) {
        return FormatUtils.getRelativeTimeSpanString(new DateTime(time));
    }

}
