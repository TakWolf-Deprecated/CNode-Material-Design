package org.cnodejs.android.md.model.entity;

import android.support.annotation.NonNull;

public class UpReplyResult extends Result {

    private Reply.UpAction action;

    @NonNull
    public Reply.UpAction getAction() {
        return action == null ? Reply.UpAction.down : action;
    }

    public void setAction(Reply.UpAction action) {
        this.action = action;
    }

}
