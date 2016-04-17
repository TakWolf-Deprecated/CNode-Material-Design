package org.cnodejs.android.md.model.entity;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Message {

    public enum Type {
        reply,
        at
    }

    private String id;

    private Type type;

    @SerializedName("has_read")
    private boolean read;

    private Author author;

    private TopicSimple topic; // 这里不含Author字段，注意

    private Reply reply; // 这里不含Author字段，注意

    @SerializedName("create_at")
    private DateTime createAt; // TODO 这个字段目前不存在，需要服务端补全

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public Type getType() {
        return type == null ? Type.reply : type; // 保证返回不为空
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public TopicSimple getTopic() {
        return topic;
    }

    public void setTopic(TopicSimple topic) {
        this.topic = topic;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public DateTime getCreateAt() { // TODO 这里做兼容处理
        //return createAt;
        if (createAt != null) {
            return createAt;
        } else if (getReply() != null && getReply().getCreateAt() != null) {
            return getReply().getCreateAt();
        } else {
            return getTopic().getLastReplyAt();
        }
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

}
