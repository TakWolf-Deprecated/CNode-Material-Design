package org.cnodejs.android.md.util;

import android.text.TextUtils;

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
     */
    private static final Markdown md = new Markdown();

    public static final String THEME_CSS = "file:///android_asset/cnode.css";

    private static final String HTML_0 = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1\">";

    private static final String HTML_1 = "\n" +
            "</head>\n" +
            "<body>\n";

    private static final String HTML_2 = "\n" +
            "</body>\n" +
            "</html>";

    public static String renderMarkdown(String text) {
        // 协议替换
        text = text.replace("](/user/", "](https://cnodejs.org/user/"); // TODO 替换@用户协议
        text = text.replace("](//dn-cnode.qbox.me/", "](https://dn-cnode.qbox.me/"); // TODO 替换CNode CDN图片路径
        // 转换
        StringWriter out = new StringWriter();
        try {
            md.transform(new StringReader(text), out);
            text = out.toString();

            // 生成完整html
            return HTML_0 + "\n<link type=\"text/css\" rel=\"stylesheet\" href=\"" + THEME_CSS + "\">" + HTML_1 + text + HTML_2;
        } catch (ParseException e) {
            return text;
        }
    }

}
