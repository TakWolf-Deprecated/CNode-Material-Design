package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

public class LoginInfo {

    private String id;

    @SerializedName("loginname")
    private String loginName;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
