package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

public class CreateTopicResult extends Result {

    @SerializedName("topic_id")
    private String topicId;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

}
