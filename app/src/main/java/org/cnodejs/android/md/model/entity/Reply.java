package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

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
        handleContent = null; // 清除已经处理的Html渲染缓存
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

    @SerializedName("handle_content")
    private String handleContent;

    public String getHandleContent() {
        if (handleContent == null) {
            if (ApiDefine.MD_RENDER) {
                handleContent = FormatUtils.handleHtml(content);
            } else {
                handleContent = FormatUtils.handleHtml(FormatUtils.renderMarkdown(content));
            }
        }
        return handleContent;
    }

    public void setContentFromLocal(String content) {
        if (ApiDefine.MD_RENDER) {
            this.content = FormatUtils.renderMarkdown(content);
        } else {
            this.content = content;
        }
        handleContent = null; // 清除已经处理的Html渲染缓存
    }

}
