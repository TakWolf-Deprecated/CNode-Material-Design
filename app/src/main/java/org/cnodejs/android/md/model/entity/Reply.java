package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;

import java.util.List;

public class Reply {

    public enum UpAction {
        up,
        down
    }

    private String id;

    private Author author;

    private String content;

    @SerializedName("ups")
    private List<String> upList;

    @SerializedName("reply_id")
    private String replyId;

    @SerializedName("create_at")
    private DateTime createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        cleanContentCache();
    }

    public List<String> getUpList() {
        return upList;
    }

    public void setUpList(List<String> upList) {
        this.upList = upList;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
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

    @SerializedName("content_summary")
    private String contentSummary;

    public void markSureHandleContent() {
        if (contentHtml == null || contentSummary == null) {
            Document document;
            if (ApiDefine.MD_RENDER) {
                document = FormatUtils.handleHtml(content);
            } else {
                document = FormatUtils.handleHtml(FormatUtils.renderMarkdown(content));
            }
            if (contentHtml == null) {
                contentHtml = document.body().html();
            }
            if (contentSummary == null) {
                contentSummary = document.body().text().trim();
            }
        }
    }

    public void cleanContentCache() {
        contentHtml = null;
        contentSummary = null;
    }

    public String getContentHtml() {
        markSureHandleContent();
        return contentHtml;
    }

    public String getContentSummary() {
        markSureHandleContent();
        return contentSummary;
    }

    public void setContentFromLocal(String content) {
        if (ApiDefine.MD_RENDER) {
            this.content = FormatUtils.renderMarkdown(content);
        } else {
            this.content = content;
        }
        cleanContentCache();
    }

}
