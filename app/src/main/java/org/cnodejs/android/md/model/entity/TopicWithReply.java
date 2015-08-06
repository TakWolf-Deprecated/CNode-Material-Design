package org.cnodejs.android.md.model.entity;

import java.util.List;

public class TopicWithReply extends Topic {

    private List<Reply> replies;

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

}
