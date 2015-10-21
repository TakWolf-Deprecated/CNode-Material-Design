package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notification {

    @SerializedName("has_read_messages")
    private List<Message> hasReadMessageList;

    @SerializedName("hasnot_read_messages")
    private List<Message> hasNotReadMessageList;

    public List<Message> getHasReadMessageList() {
        return hasReadMessageList;
    }

    public void setHasReadMessageList(List<Message> hasReadMessageList) {
        this.hasReadMessageList = hasReadMessageList;
    }

    public List<Message> getHasNotReadMessageList() {
        return hasNotReadMessageList;
    }

    public void setHasNotReadMessageList(List<Message> hasNotReadMessageList) {
        this.hasNotReadMessageList = hasNotReadMessageList;
    }

}
