package org.cnodejs.android.md.util;

import com.petebevin.markdown.MarkdownProcessor;

public final class MarkdownUtils {

    private MarkdownUtils() {}

    private static final MarkdownProcessor mp = new MarkdownProcessor();

    public static String markdown(String text) {
        return mp.markdown(text);
    }

}
