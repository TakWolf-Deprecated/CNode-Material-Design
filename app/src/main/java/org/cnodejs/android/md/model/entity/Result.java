package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

public class Result {

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public static class Data<T> extends Result {

        private T data;

        public T getData() {
            return data;
        }

    }

    public static class CreateTopic extends Result {

        @SerializedName("topic_id")
        private String topicId;

        public String getTopicId() {
            return topicId;
        }

    }

    public static class ReplyTopic extends Result {

        @SerializedName("reply_id")
        private String replyId;

        public String getReplyId() {
            return replyId;
        }

    }

}
