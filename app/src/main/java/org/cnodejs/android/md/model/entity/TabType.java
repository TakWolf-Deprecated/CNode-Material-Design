package org.cnodejs.android.md.model.entity;

import org.cnodejs.android.md.R;

public enum TabType {

    all(R.string.tab_all, R.id.main_left_btn_all),

    good(R.string.tab_good, R.id.main_left_btn_good),

    share(R.string.tab_share, R.id.main_left_btn_share),

    ask(R.string.tab_ask, R.id.main_left_btn_ask),

    job(R.string.tab_job, R.id.main_left_btn_job);

    private int nameId;

    private int actionId;

    TabType(int nameId, int actionId) {
        this.nameId = nameId;
        this.actionId = actionId;
    }

    public int getNameId() {
        return nameId;
    }

    public int getActionId() {
        return actionId;
    }

    public static TabType actionIdOf(int actionId) {
        switch (actionId) {
            case R.id.main_left_btn_all:
                return all;
            case R.id.main_left_btn_good:
                return good;
            case R.id.main_left_btn_share:
                return share;
            case R.id.main_left_btn_ask:
                return ask;
            case R.id.main_left_btn_job:
                return job;
            default:
                return all;
        }

    }

}
