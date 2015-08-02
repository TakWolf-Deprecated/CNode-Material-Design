package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

public class LoginInfo {

    private boolean success;

    @SerializedName("loginname")
    private String loginName;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

}
