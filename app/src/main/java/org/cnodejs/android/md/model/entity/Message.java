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
    private DateTime createAt;

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

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

}
