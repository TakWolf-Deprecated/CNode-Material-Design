package org.cnodejs.android.md.model.entity;

import android.support.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.model.util.EntityUtils;
import org.cnodejs.android.md.util.FormatUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

public class Result {

    protected boolean success;

    public boolean isSuccess() {
        return success;
    }

    public static class Data<T> extends Result {

        private T data;

        public T getData() {
            return data;
        }

    }

    public static class Error extends Result {

        @SerializedName("error_msg")
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

    }

    public static <T extends Result> Error buildError(@NonNull Response<T> response) {
        try {
            return EntityUtils.gson.fromJson(response.errorBody().string(), Error.class);
        } catch (IOException | JsonSyntaxException e) {
            Error error = new Error();
            error.success = false;
            switch (response.code()) {
                case 400:
                    error.errorMessage = "请求参数有误";
                    break;
                case 403:
                    error.errorMessage = "请求被拒绝";
                    break;
                case 404:
                    error.errorMessage = "资源未找到";
                    break;
                case 405:
                    error.errorMessage = "请求方式不被允许";
                    break;
                case 408:
                    error.errorMessage = "请求超时";
                    break;
                case 422:
                    error.errorMessage = "请求语义错误";
                    break;
                case 500:
                    error.errorMessage = "服务器逻辑错误";
                    break;
                case 502:
                    error.errorMessage = "服务器网关错误";
                    break;
                case 504:
                    error.errorMessage = "服务器网关超时";
                    break;
                default:
                    error.errorMessage = response.message();
                    break;
            }
            return error;
        }
    }

    public static Error buildError(@NonNull Throwable t) {
        Error error = new Error();
        error.success = false;
        if (t instanceof UnknownHostException) {
            error.errorMessage = "网络无法连接";
        } else if (t instanceof SocketTimeoutException) {
            error.errorMessage = "网络访问超时";
        } else if (t instanceof JsonSyntaxException) {
            error.errorMessage = "响应数据格式错误";
        } else {
            error.errorMessage = "未知错误：" + t.getLocalizedMessage();
        }
        return error;
    }

    public static class Login extends Result {

        private String id;

        @SerializedName("loginname")
        private String loginName;

        @SerializedName("avatar_url")
        private String avatarUrl;

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getAvatarUrl() { // 修复头像地址的历史遗留问题
            return FormatUtils.getCompatAvatarUrl(avatarUrl);
        }

    }

    public static class CreateTopic extends Result {

        @SerializedName("topic_id")
        private String topicId;

        public String getTopicId() {
            return topicId;
        }

    }

    public static class ReplyTopic extends Result {

        @SerializedName("reply_id")
        private String replyId;

        public String getReplyId() {
            return replyId;
        }

    }

    public static class UpReply extends Result {

        private Reply.UpAction action;

        public Reply.UpAction getAction() {
            return action == null ? Reply.UpAction.down : action;
        }

    }

}
