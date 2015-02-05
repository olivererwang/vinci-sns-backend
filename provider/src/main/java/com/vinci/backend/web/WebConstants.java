package com.vinci.backend.web;

import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class WebConstants {
    public final static int MAX_LENGTH_OF_GET_USER_BY_IDS = 50;

    public final static ErrorCode ERROR_UNKNOWN_ERROR = new ErrorCode(ErrorType.WebArgumentErrorType, 999, "系统繁忙，请稍后再试");
    public final static ErrorCode ERROR_Missing_Servlet_Request_Parameter = new ErrorCode(ErrorType.WebArgumentErrorType, 998, "缺少%s参数");
    public final static ErrorCode ERROR_Type_Miss_Match_Servlet_Request_Parameter = new ErrorCode(ErrorType.WebArgumentErrorType, 997, "参数类型错误%s");
    public final static ErrorCode ERROR_DEFAULT_ARGUMENT_ERROR = new ErrorCode(ErrorType.WebArgumentErrorType, 997, "参数错误");

    public final static ErrorCode ERROR_NEED_LOGIN = new ErrorCode(ErrorType.WebArgumentErrorType, 1, "需要登录");
    public final static ErrorCode ERROR_USER_IS_NULL = new ErrorCode(ErrorType.WebArgumentErrorType, 2, "用户内容为空");
    public final static ErrorCode ERROR_USER_NICKNAME_IS_NULL = new ErrorCode(ErrorType.WebArgumentErrorType, 3, "用户昵称为空");
    public final static ErrorCode ERROR_USER_IS_NOT_EXIST = new ErrorCode(ErrorType.WebArgumentErrorType, 4, "用户不存在");
    public final static ErrorCode ERROR_USER_ID_ARGUMENT_INVALID = new ErrorCode(ErrorType.WebArgumentErrorType, 5, "传入的userid错误");
    public final static ErrorCode ERROR_USER_ID_ARGUMENT_IS_TOO_LONG = new ErrorCode(ErrorType.WebArgumentErrorType, 6, "最大获取用户数" + MAX_LENGTH_OF_GET_USER_BY_IDS);
    public final static ErrorCode ERROR_FEED_IS_NULL = new ErrorCode(ErrorType.WebArgumentErrorType, 6, "要分享的内容为空");

}
