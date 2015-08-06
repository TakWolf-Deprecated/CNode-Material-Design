package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.entity.LoginInfo;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicUpInfo;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.entity.User;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiService {

    //=====
    // 主题
    //=====

    @GET("/v1/topics")
    void getTopics(
            @Query("tab") TabType tab,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("mdrender") Boolean mdrender,
            Callback<Result<List<Topic>>> callback
    );

    @GET("/v1/topic/{id}")
    void getTopic(
            @Path("id") String id,
            @Query("mdrender") Boolean mdrender,
            Callback<Result<TopicWithReply>> callback
    );

    @FormUrlEncoded
    @POST("/v1/topics")
    void newTopic(
            @Field("accesstoken") String accessToken,
            @Field("tab") TabType tab,
            @Field("title") String title,
            @Field("content") String content,
            Callback<Void> callback
    );

    @FormUrlEncoded
    @POST("/v1/topic/collect")
    void collectTopic(
            @Field("accesstoken") String accessToken,
            @Field("topic_id") String topicId,
            Callback<Void> callback
    );

    @FormUrlEncoded
    @POST("/v1/topic/de_collect")
    void decollectTopic(
            @Field("accesstoken") String accessToken,
            @Field("topic_id") String topicId,
            Callback<Void> callback
    );

    @FormUrlEncoded
    @POST("/v1/topic/{topicId}/replies")
    void replyTopic(
            @Field("accesstoken") String accessToken,
            @Path("topicId") String topicId,
            @Field("content") String content,
            @Field("reply_id") String replyId,
            Callback<Map<String, String>> callback
    );

    @FormUrlEncoded
    @POST("/v1/reply/{replyId}/ups")
    void upTopic(
            @Field("accesstoken") String accessToken,
            @Path("replyId") String replyId,
            Callback<TopicUpInfo> callback
    );

    //=====
    // 用户
    //=====

    @GET("/v1/user/{loginName}")
    void getUser(
            @Path("loginName") String loginName,
            Callback<Result<User>> callback
    );

    @FormUrlEncoded
    @POST("/v1/accesstoken")
    void accessToken(
            @Field("accesstoken") String accessToken,
            Callback<LoginInfo> callback
    );

    //=========
    // 消息通知
    //=========

    @GET("/v1/message/count")
    void getMessageCount(
            @Query("accesstoken") String accessToken,
            Callback<Result<Integer>> callback
    );

    @GET("/v1/messages")
    void getMessages(
            @Query("accesstoken") String accessToken,
            Callback<Result<Notification>> callback
    );

    @FormUrlEncoded
    @POST("/v1/message/mark_all")
    void markAllMessageRead(
            @Field("accesstoken") String accessToken,
            Callback<Void> callback
    );

}
