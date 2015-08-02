package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.entity.LoginInfo;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;

import java.util.List;

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

    //=====
    // 用户
    //=====

    @FormUrlEncoded
    @POST("/v1/accesstoken")
    void accessToken(
            @Field("accesstoken") String accessToken,
            Callback<LoginInfo> callback
    );

}
