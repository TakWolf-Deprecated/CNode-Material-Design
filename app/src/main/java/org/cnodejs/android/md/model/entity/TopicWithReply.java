package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicWithReply extends Topic {

    @SerializedName("replies")
    private List<Reply> replyList;

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

}
