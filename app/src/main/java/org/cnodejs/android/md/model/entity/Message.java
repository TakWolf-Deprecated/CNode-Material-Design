package org.cnodejs.android.md.model.entity;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Message {

    private String id;

    private MessageType type;

    @SerializedName("has_read")
    private boolean read;

    private Author author;

    private TopicSimple topic; // 这里不含Author字段，注意

    private Reply reply; // 这里不含Author字段，注意

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public MessageType getType() {
        return type == null ? MessageType.normal : type; // 保证返回不为空
    }

    public void setType(MessageType type) {
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

}
