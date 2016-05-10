package org.cnodejs.android.md.model.storage;

import android.content.Context;

public final class TopicShared {

    private TopicShared() {}

    private static final String TAG = "TopicShared";

    private static String getSharedName(Context context) {
        return TAG + "@" + LoginShared.getId(context);
    }

    private static final String KEY_DRAFT_TAB_POSITION = "draft_tab_position";
    private static final String KEY_DRAFT_TITLE = "draft_title";
    private static final String KEY_DRAFT_CONTENT = "draft_content";

    public static void clear(Context context) {
        SharedWrapper.with(context, getSharedName(context)).clear();
    }

    public static int getDraftTabPosition(Context context) {
        return SharedWrapper.with(context, getSharedName(context)).getInt(KEY_DRAFT_TAB_POSITION, 0);
    }

    public static void setDraftTabPosition(Context context, int position) {
        SharedWrapper.with(context, getSharedName(context)).setInt(KEY_DRAFT_TAB_POSITION, position);
    }

    public static String getDraftTitle(Context context) {
        return SharedWrapper.with(context, getSharedName(context)).getString(KEY_DRAFT_TITLE, null);
    }

    public static void setDraftTitle(Context context, String title) {
        SharedWrapper.with(context, getSharedName(context)).setString(KEY_DRAFT_TITLE, title);
    }

    public static String getDraftContent(Context context) {
        return SharedWrapper.with(context, getSharedName(context)).getString(KEY_DRAFT_CONTENT, null);
    }

    public static void setDraftContent(Context context, String content) {
        SharedWrapper.with(context, getSharedName(context)).setString(KEY_DRAFT_CONTENT, content);
    }

}
