package org.cnodejs.android.md.util;

import android.text.TextUtils;

import org.cnodejs.android.md.ui.activity.SettingActivity;
import org.joda.time.DateTime;
import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

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
        }
        long diff = new Date().getTime() - dateTime.getMillis();
        long r;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > week) {
            r = (diff / week);
            return r + "周前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
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
        StringWriter out = new StringWriter();
        try {
            md.transform(new StringReader(text), out);
            text = out.toString();
        } catch (ParseException e) {
            // nothing to do
        }
        return "<div class=\"markdown-text\">" + text + "</div>";
    }

    public static String handleHtml(String text) {
        text = text.replace("<a href=\"/user/", "<a href=\"https://cnodejs.org/user/"); // TODO 替换@用户协议
        text = text.replace("<img src=\"//", "<img src=\"https://"); // TODO 替换缩略URL引用路径为https协议
        return text;
    }

}
