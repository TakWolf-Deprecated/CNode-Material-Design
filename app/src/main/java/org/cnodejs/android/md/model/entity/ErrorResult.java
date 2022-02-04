package org.cnodejs.android.md.model.entity;

import android.support.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import org.cnodejs.android.md.model.util.EntityUtils;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ErrorResult extends Result {

    @NonNull
    public static ErrorResult from(@NonNull Response response) {
        ErrorResult errorResult = null;
        ResponseBody errorBody = response.errorBody();
        if (errorBody != null) {
            try {
                errorResult = EntityUtils.gson.fromJson(errorBody.string(), ErrorResult.class);
            } catch (Throwable ignored) {}
        }
        if (errorResult == null) {
            errorResult = new ErrorResult();
            errorResult.setSuccess(false);
            switch (response.code()) {
                case 400:
                    errorResult.setMessage("非法请求");
                    break;
                case 403:
                    errorResult.setMessage("非法行为");
                    break;
                case 404:
                    errorResult.setMessage("未找到资源");
                    break;
                case 405:
                    errorResult.setMessage("非法请求方式");
                    break;
                case 408:
                    errorResult.setMessage("请求超时");
                    break;
                case 500:
                    errorResult.setMessage("服务器内部错误");
                    break;
                case 502:
                    errorResult.setMessage("服务器网关错误");
                    break;
                case 504:
                    errorResult.setMessage("服务器网关超时");
                    break;
                default:
                    errorResult.setMessage("未知响应：" + response.message());
                    break;
            }
        }
        return errorResult;
    }

    @NonNull
    public static ErrorResult from(@NonNull Throwable t) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setSuccess(false);
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            errorResult.setMessage("网络无法连接");
        } else if (t instanceof NoRouteToHostException) {
            errorResult.setMessage("无法访问网络");
        } else if (t instanceof SocketTimeoutException) {
            errorResult.setMessage("网络访问超时");
        } else if (t instanceof JsonSyntaxException) {
            errorResult.setMessage("响应数据格式错误");
        } else {
            errorResult.setMessage("未知错误：" + t.getLocalizedMessage());
        }
        return errorResult;
    }

    @SerializedName("error_msg")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
