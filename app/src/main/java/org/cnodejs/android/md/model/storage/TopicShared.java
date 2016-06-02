package org.cnodejs.android.md.model.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class TopicShared {

    private TopicShared() {}

    private static final String TAG = "TopicShared";

    private static String getSharedName(@NonNull Context context) {
        return TAG + "@" + LoginShared.getId(context);
    }

    private static final String KEY_DRAFT_TAB_POSITION = "draftTabPosition";
    private static final String KEY_DRAFT_TITLE = "draftTitle";
    private static final String KEY_DRAFT_CONTENT = "draftContent";

    public static void clear(@NonNull Context context) {
        SharedWrapper.with(context, getSharedName(context)).clear();
    }

    public static int getDraftTabPosition(@NonNull Context context) {
        return SharedWrapper.with(context, getSharedName(context)).getInt(KEY_DRAFT_TAB_POSITION, 0);
    }

    public static void setDraftTabPosition(@NonNull Context context, int position) {
        SharedWrapper.with(context, getSharedName(context)).setInt(KEY_DRAFT_TAB_POSITION, position);
    }

    public static String getDraftTitle(@NonNull Context context) {
        return SharedWrapper.with(context, getSharedName(context)).getString(KEY_DRAFT_TITLE, null);
    }

    public static void setDraftTitle(@NonNull Context context, @Nullable String title) {
        SharedWrapper.with(context, getSharedName(context)).setString(KEY_DRAFT_TITLE, title);
    }

    public static String getDraftContent(@NonNull Context context) {
        return SharedWrapper.with(context, getSharedName(context)).getString(KEY_DRAFT_CONTENT, null);
    }

    public static void setDraftContent(@NonNull Context context, @Nullable String content) {
        SharedWrapper.with(context, getSharedName(context)).setString(KEY_DRAFT_CONTENT, content);
    }

}
