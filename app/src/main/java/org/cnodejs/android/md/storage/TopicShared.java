package org.cnodejs.android.md.storage;

import android.content.Context;

public final class TopicShared {

    private TopicShared() {}

    private static final String TAG = TopicShared.class.getSimpleName(); // TODO 这里需要添加当前用户loginName，避免用户数据混淆

    private static final String KEY_NEW_TOPIC_TAB_POSITION = "new_topic_tab_position";
    private static final String KEY_NEW_TOPIC_TITLE = "new_topic_title";
    private static final String KEY_NEW_TOPIC_CONTENT = "new_topic_content";

    private static String getTag(Context context) {
        return TAG + "@" + LoginShared.getLoginName(context);
    }

    public static void clear(Context context) {
        SharedWrapper.with(context, getTag(context)).clear();
    }

    public static int getNewTopicTabPosition(Context context) {
        return SharedWrapper.with(context, getTag(context)).getInt(KEY_NEW_TOPIC_TAB_POSITION, 0);
    }

    public static void setNewTopicTabPosition(Context context, int position) {
        SharedWrapper.with(context, getTag(context)).setInt(KEY_NEW_TOPIC_TAB_POSITION, position);
    }

    public static String getNewTopicTitle(Context context) {
        return SharedWrapper.with(context, getTag(context)).getString(KEY_NEW_TOPIC_TITLE, null);
    }

    public static void setNewTopicTitle(Context context, String title) {
        SharedWrapper.with(context, getTag(context)).setString(KEY_NEW_TOPIC_TITLE, title);
    }

    public static String getNewTopicContent(Context context) {
        return SharedWrapper.with(context, getTag(context)).getString(KEY_NEW_TOPIC_CONTENT, null);
    }

    public static void setNewTopicContent(Context context, String content) {
        SharedWrapper.with(context, getTag(context)).setString(KEY_NEW_TOPIC_CONTENT, content);
    }

}
