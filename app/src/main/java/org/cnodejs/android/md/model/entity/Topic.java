package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

public class Topic extends TopicSimple {

    @SerializedName("author_id")
    private String authorId;

    private TabType tab;

    private String content;

    private boolean good;

    private boolean top;

    @SerializedName("reply_count")
    private int replyCount;

    @SerializedName("visit_count")
    private int visitCount;

    @SerializedName("create_at")
    private DateTime createAt;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public TabType getTab() {
        return tab == null ? TabType.all : tab; // 接口中有些话题没有Tab属性，这里保证Tab不为空
    }

    public void setTab(TabType tab) {
        this.tab = tab;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        contentHtml = null; // 清除已经处理的Html渲染缓存
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    /**
     * Html渲染缓存
     */

    @SerializedName("content_html")
    private String contentHtml;

    public String getContentHtml() {
        if (contentHtml == null) {
            if (ApiDefine.MD_RENDER) {
                contentHtml = FormatUtils.handleHtml(content);
            } else {
                contentHtml = FormatUtils.handleHtml(FormatUtils.renderMarkdown(content));
            }
        }
        return contentHtml;
    }

}
