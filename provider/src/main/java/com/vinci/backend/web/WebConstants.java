package com.vinci.backend.web;

import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class WebConstants {
    public final static ErrorCode ERROR_UNKNOWN_ERROR = new ErrorCode(ErrorType.WebArgumentErrorType, 999, "系统繁忙，请稍后再试");
    public final static ErrorCode ERROR_Missing_Servlet_Request_Parameter = new ErrorCode(ErrorType.WebArgumentErrorType, 998, "缺少%s参数");
    public final static ErrorCode ERROR_DEFAULT_ARGUMENT_ERROR = new ErrorCode(ErrorType.WebArgumentErrorType, 997, "参数错误");

    public final static ErrorCode ERROR_NEED_LOGIN = new ErrorCode(ErrorType.WebArgumentErrorType, 1, "需要登录");
    public final static ErrorCode ERROR_USER_IS_NULL = new ErrorCode(ErrorType.WebArgumentErrorType, 2, "用户内容为空");
    public final static ErrorCode ERROR_USER_NICKNAME_IS_NULL = new ErrorCode(ErrorType.WebArgumentErrorType, 3, "用户昵称为空");



}
