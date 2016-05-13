package org.cnodejs.android.md.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.cnodejs.android.md.model.api.ApiDefine;
import org.joda.time.DateTime;
import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

public final class FormatUtils {

    private FormatUtils() {}

    /**
     * 获取最近时间字符串
     */

    private static final long MINUTE = 60 * 1000;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 31 * DAY;
    private static final long YEAR = 12 * MONTH;

    public static String getRecentlyTimeText(@NonNull DateTime dateTime) {
        long offset = new DateTime().getMillis() - dateTime.getMillis();
        if (offset > YEAR) {
            return (offset / YEAR) + "年前";
        } else if (offset > MONTH) {
            return (offset / MONTH) + "个月前";
        } else if (offset > WEEK) {
            return (offset / WEEK) + "周前";
        } else if (offset > DAY) {
            return (offset / DAY) + "天前";
        } else if (offset > HOUR) {
            return (offset / HOUR) + "小时前";
        } else if (offset > MINUTE) {
            return (offset / MINUTE) + "分钟前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 检测是否是用户accessToken
     */
    public static boolean isAccessToken(String accessToken) {
        if (TextUtils.isEmpty(accessToken)) {
            return false;
        } else {
            try {
                //noinspection ResultOfMethodCallIgnored
                UUID.fromString(accessToken);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 生成一个不重复的requestCode
     */

    private static int requestCodeSeed = 0;

    public synchronized static int createRequestCode() {
        requestCodeSeed++;
        return requestCodeSeed;
    }

    /**
     * 获取菜单导航消息数目字符串
     */
    public static String getNavigationDisplayCountText(int count) {
        if (count > 99) {
            return "99+";
        } else if (count <= 0) {
            return "";
        } else {
            return String.valueOf(count);
        }
    }

    /**
     * 修复头像地址的历史遗留问题
     */
    public static String getCompatAvatarUrl(String avatarUrl) {
        if (!TextUtils.isEmpty(avatarUrl) && avatarUrl.startsWith("//gravatar.com/avatar/")) {
            return "http:" + avatarUrl;
        } else {
            return avatarUrl;
        }
    }

    /**
     * 标准URL检测
     */

    public static boolean isUserLinkUrl(@Nullable String url) {
        return !TextUtils.isEmpty(url) && url.startsWith(ApiDefine.USER_LINK_URL_PREFIX);
    }

    public static boolean isTopicLinkUrl(@Nullable String url) {
        return !TextUtils.isEmpty(url) && url.startsWith(ApiDefine.TOPIC_LINK_URL_PREFIX);
    }

    /**
     * CNode兼容性的Markdown转换
     * 最外层包裹 <div class="markdown-text"></div> 以保证和服务端渲染同步
     */
    private static final Markdown md = new Markdown();

    public static String renderMarkdown(String text) {
        // 保证text不为null
        text = TextUtils.isEmpty(text) ? "" : text;
        // 解析@协议
        text = " " + text;
        text = text.replaceAll(" @([a-zA-Z0-9_-]+)", "\\[@$1\\]\\(" + ApiDefine.USER_PATH_PREFIX + "$1\\)").trim();
        // 渲染markdown
        try {
            StringWriter out = new StringWriter();
            md.transform(new StringReader(text), out);
            text = out.toString();
        } catch (ParseException e) {
            // nothing to do
        }
        // 添加样式容器
        return "<div class=\"markdown-text\">" + text + "</div>";
    }

    public static String handleHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            html = html.replace("<a href=\"" + ApiDefine.USER_PATH_PREFIX, "<a href=\"" + ApiDefine.USER_LINK_URL_PREFIX); // 替换@用户协议
            html = html.replace("<img src=\"//", "<img src=\"https://"); // 替换缩略URL引用路径为https协议
        }
        return html;
    }

}
