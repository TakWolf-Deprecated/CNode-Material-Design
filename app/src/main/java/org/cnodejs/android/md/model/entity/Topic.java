package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

public class Topic {

    private String id;

    @SerializedName("author_id")
    private String authorId;

    private Author author;

    private TabType tab;

    private String title;

    private String content;

    @SerializedName("last_reply_at")
    private DateTime lastReplyAt;

    private boolean good;

    private boolean top;

    @SerializedName("reply_count")
    private int replyCount;

    @SerializedName("visit_count")
    private int visitCount;

    @SerializedName("create_at")
    private DateTime createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public TabType getTab() {
        return tab == null ? TabType.all : tab; // TODO 接口中有些话题没有Tab属性，这里保证Tab不为空
    }

    public void setTab(TabType tab) {
        this.tab = tab;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DateTime getLastReplyAt() {
        return lastReplyAt;
    }

    public void setLastReplyAt(DateTime lastReplyAt) {
        this.lastReplyAt = lastReplyAt;
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

    // TODO Markdown渲染缓存
    private String renderedContent = null;

    public String getRenderedContent() {
        if (renderedContent == null) { // 需要渲染
            renderedContent = FormatUtils.renderMarkdown(getContent());
        }
        return renderedContent;
    }

}
