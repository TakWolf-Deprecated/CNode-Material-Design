package org.cnodejs.android.md.model.entity;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.util.MarkdownUtils;
import org.joda.time.DateTime;

import java.util.List;

public class Reply {

    private String id;

    private Author author;

    private String content;

    private List<String> ups;

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
    }

    public List<String> getUps() {
        return ups;
    }

    public void setUps(List<String> ups) {
        this.ups = ups;
    }

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    // TODO 默认消息回复是不渲染的，这里提供一个方法检测是否渲染并渲染
    private Spanned spannedContent = null;

    public Spanned makeSureRenderAndGetSpannedContent() {
        if (spannedContent == null) { // 需要渲染
            spannedContent = MarkdownUtils.spanned(getContent());
        }
        return spannedContent;
    }

}
