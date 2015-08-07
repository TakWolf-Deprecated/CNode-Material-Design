package org.cnodejs.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;
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

    @SerializedName("collect_topics")
    private List<TopicSimple> collectTopics;

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
        return recentTopics == null ? new ArrayList<TopicSimple>() : recentTopics; // TODO 防止为空
    }

    public void setRecentTopics(List<TopicSimple> recentTopics) {
        this.recentTopics = recentTopics;
    }

    public List<TopicSimple> getRecentReplies() {
        return recentReplies == null ? new ArrayList<TopicSimple>() : recentReplies; // TODO 防止为空
    }

    public void setRecentReplies(List<TopicSimple> recentReplies) {
        this.recentReplies = recentReplies;
    }

    public List<TopicSimple> getCollectTopics() {
        return collectTopics == null ? new ArrayList<TopicSimple>() : collectTopics; // TODO 防止为空
    }

    public void setCollectTopics(List<TopicSimple> collectTopics) {
        this.collectTopics = collectTopics;
    }

}
