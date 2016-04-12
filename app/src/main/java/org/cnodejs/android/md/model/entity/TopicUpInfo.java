package org.cnodejs.android.md.model.entity;

public class TopicUpInfo extends Result {

    public enum Action {
        up,
        down
    }

    private Action action;

    public Action getAction() {
        return action == null ? Action.down : action;
    }

}
