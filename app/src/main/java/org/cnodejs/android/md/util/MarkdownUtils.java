package org.cnodejs.android.md.util;

import com.petebevin.markdown.MarkdownProcessor;

public final class MarkdownUtils {

    private MarkdownUtils() {}

    public static final String THEME_CSS = "file:///android_asset/cnode.css";

    private static final MarkdownProcessor mp = new MarkdownProcessor();

    public static String cnodeFilter(String text) {
        text = text.replace("](/user/", "](https://cnodejs.org/user/"); // TODO 替换@用户协议
        text = text.replace("](//dn-cnode.qbox.me/", "](https://dn-cnode.qbox.me/"); // TODO 替换CNode CDN图片路径
        return text;
    }

    public static String markdown(String text) {
        return mp.markdown(cnodeFilter(text));
    }

}
