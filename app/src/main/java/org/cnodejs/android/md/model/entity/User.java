package org.cnodejs.android.md.model.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

import java.util.List;

public class User {

    @SerializedName("loginname")
    private String loginName;

    @SerializedName("avatar_url")
    private String avatarUrl;

    private String githubUsername;

    @SerializedName("create_at")
    private DateTime createAt;

    private int score;

    @SerializedName("recent_topics")
    private List<TopicSimple> recentTopics;

    @SerializedName("recent_replies")
    private List<TopicSimple> recentReplies;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAvatarUrl() { // TODO 修复头像地址的历史遗留问题
        return FormatUtils.getCompatAvatarUrl(avatarUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<TopicSimple> getRecentTopics() {
        return recentTopics;
    }

    public void setRecentTopics(List<TopicSimple> recentTopics) {
        this.recentTopics = recentTopics;
    }

    public List<TopicSimple> getRecentReplies() {
        return recentReplies;
    }

    public void setRecentReplies(List<TopicSimple> recentReplies) {
        this.recentReplies = recentReplies;
    }

}
