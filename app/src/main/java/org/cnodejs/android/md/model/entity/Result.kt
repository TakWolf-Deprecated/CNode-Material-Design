package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import org.cnodejs.android.md.util.JsonUtils
import retrofit2.HttpException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@JsonClass(generateAdapter = true)
data class ApiResult(
    @Json(name = "success") val isSuccessful: Boolean,
)

@JsonClass(generateAdapter = true)
data class ErrorResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "error_msg") val message: String,
) {
    companion object {
        fun withMessage(message: String): ErrorResult {
            return ErrorResult(false, message)
        }

        fun from(e: Exception): ErrorResult {
            if (e is HttpException) {
                try {
                    e.response()?.errorBody()?.string()?.let { json ->
                        JsonUtils.moshi.adapter(ErrorResult::class.java).fromJson(json)?.let { errorResult ->
                            return errorResult
                        }
                    }
                } catch (_: Exception) {}
                return when (e.code()) {
                    400 -> withMessage("请求参数错误")
                    403 -> withMessage("无权限")
                    404 -> withMessage("资源不存在")
                    500 -> withMessage("服务器内部错误")
                    502 -> withMessage("服务器网关错误")
                    else -> withMessage("网络响应错误：${e.code()}")
                }
            } else if (e is ConnectException) {
                return withMessage("网络连接失败")
            } else if (e is UnknownHostException || e is NoRouteToHostException) {
                return withMessage("无法访问服务器")
            } else if (e is SocketTimeoutException) {
                return withMessage("网络访问超时")
            } else if (e is JsonDataException || e is JsonEncodingException) {
                return withMessage("响应数据格式错误")
            } else {
                return withMessage("未知错误：${e.localizedMessage}")
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class DataResult<Data>(
    @Json(name = "success") val isSuccessful: Boolean,
    val data: Data,
)

@JsonClass(generateAdapter = true)
data class TopicIdResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "topic_id") val topicId: String,
)

@JsonClass(generateAdapter = true)
data class ReplyIdResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "reply_id") val replyId: String,
)

@JsonClass(generateAdapter = true)
data class UpReplyResult(
    @Json(name = "success") val isSuccessful: Boolean,
    val action: UpAction,
)

@JsonClass(generateAdapter = true)
data class LoginResult(
    @Json(name = "success") val isSuccessful: Boolean,
    val id: String,
    @Json(name = "loginname") val loginName: String,
    @Json(name = "avatar_url") val avatarUrl: String,
)

@JsonClass(generateAdapter = true)
data class MessagesMarkedAllResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "marked_msgs") val markedMessages: List<IdData>,
)

@JsonClass(generateAdapter = true)
data class MessageMarkedResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "marked_msg_id") val markedMessageId: String,
)
