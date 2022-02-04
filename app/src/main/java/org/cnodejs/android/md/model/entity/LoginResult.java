package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.util.FormatUtils;

public class LoginResult extends Result {

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

    public String getAvatarUrl() { // 修复头像地址的历史遗留问题
        return FormatUtils.getCompatAvatarUrl(avatarUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
