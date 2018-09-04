package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

public class ReplyTopicResult extends Result {

    @SerializedName("reply_id")
    private String replyId;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

}
