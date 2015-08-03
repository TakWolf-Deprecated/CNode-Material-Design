package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notification {

    @SerializedName("has_read_messages")
    private List<Message> hasReadMessages;

    @SerializedName("hasnot_read_messages")
    private List<Message> hasNotReadMessages;

    public List<Message> getHasReadMessages() {
        return hasReadMessages;
    }

    public void setHasReadMessages(List<Message> hasReadMessages) {
        this.hasReadMessages = hasReadMessages;
    }

    public List<Message> getHasNotReadMessages() {
        return hasNotReadMessages;
    }

    public void setHasNotReadMessages(List<Message> hasNotReadMessages) {
        this.hasNotReadMessages = hasNotReadMessages;
    }

}
