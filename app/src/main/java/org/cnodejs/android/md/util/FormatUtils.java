package org.cnodejs.android.md.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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

    private static final long minute = 60 * 1000;
    private static final long hour = 60 * minute;
    private static final long day = 24 * hour;
    private static final long week = 7 * day;
    private static final long month = 31 * day;
    private static final long year = 12 * month;

    public static String getRecentlyTimeText(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            long diff = new DateTime().getMillis() - dateTime.getMillis();
            if (diff > year) {
                return (diff / year) + "年前";
            } else if (diff > month) {
                return (diff / month) + "个月前";
            } else if (diff > week) {
                return (diff / week) + "周前";
            } else if (diff > day) {
                return (diff / day) + "天前";
            } else if (diff > hour) {
                return (diff / hour) + "小时前";
            } else if (diff > minute) {
                return (diff / minute) + "分钟前";
            } else {
                return "刚刚";
            }
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
     * CNode兼容性的Markdown转换
     * 最外层包裹 <div class="markdown-text"></div> 以保证和服务端渲染同步
     */
    private static final Markdown md = new Markdown();

    public static String renderMarkdown(String text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        try {
            StringWriter out = new StringWriter();
            md.transform(new StringReader(text), out);
            text = out.toString();
        } catch (ParseException e) {
            // nothing to do
        }
        return "<div class=\"markdown-text\">" + text + "</div>";
    }

    public static String handleHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            html = html.replace("<a href=\"/user/", "<a href=\"https://cnodejs.org/user/"); // 替换@用户协议
            html = html.replace("<img src=\"//", "<img src=\"https://"); // 替换缩略URL引用路径为https协议
        }
        return html;
    }

}
