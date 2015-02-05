package com.vinci.backend.web.util;

import com.vinci.backend.web.WebConstants;
import com.vinci.backend.web.user.UserContext;
import com.vinci.backend.web.user.UserInfo;
import com.vinci.common.base.api.APIResponse;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.i18n.I18NResource;
import com.vinci.common.base.i18n.MessageType;
import com.vinci.common.base.monitor.QMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static com.vinci.backend.web.WebConstants.*;

@Controller
public abstract class BaseController {
    private static Logger LOG = LoggerFactory.getLogger(BaseController.class);

    @Resource
    private I18NResource resource;

    /**
     * 未知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public APIResponse<?> handleUnexpectedException(Exception e) {
        LOG.warn("BaseController Error", e);
        QMonitor.recordOne("BaseController_UnexpectedException");
        return convertErrorCode(WebConstants.ERROR_UNKNOWN_ERROR);
    }

    /**
     * 需检查的OrderException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public APIResponse<?> handleOrderException(BizException e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("BaseController BizException", e);
        }
        QMonitor.recordOne("BaseController_BizException");
        return convertErrorCode(e.getErrorCode());
    }

    /**
     * 参数无效错误处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public APIResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("BaseController IllegalArgumentException", e);
        }
        QMonitor.recordOne("BaseController_IllegalArgumentException");
        return convertErrorCode(ERROR_DEFAULT_ARGUMENT_ERROR);
    }

    /**
     * 使用注解RequestParam的参数如果没有传递时处理异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public APIResponse<?> handleMissArgumentException(MissingServletRequestParameterException e) {
        QMonitor.recordOne("BaseController_MissArgumentException");
        return convertErrorCode(ERROR_Missing_Servlet_Request_Parameter.copy(e.getParameterName()));
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public APIResponse<?> handleMissArgumentException(TypeMismatchException e) {
        QMonitor.recordOne("BaseController_TypeMismatchException");
        return convertErrorCode(ERROR_Type_Miss_Match_Servlet_Request_Parameter.copy(e.getPropertyName()));
    }

    protected void checkLogin() {
        if (queryUserInfo() == null) {
            throw new BizException(ERROR_NEED_LOGIN);
        }
    }

    /**
     * 返回当前线程用户上下文
     *
     * @return
     */
    protected UserInfo queryUserInfo() {
        return UserContext.getUserInfo();
    }

    /**
     * 转换errorCode, 子类可以重载这个方法以实现不同的接口的返回值
     *
     * @return
     */
    protected <T> APIResponse<T> convertErrorCode(ErrorCode errorCode) {
        if (errorCode == null) {
            errorCode = ERROR_UNKNOWN_ERROR;
        }
        return APIResponse.returnFail(errorCode.getCode(), getLocaleMessage(errorCode));
    }

    protected String getLocaleMessage(ErrorCode errorCode) {
        if (errorCode == null) {
            return "unknown error";
        }
        String message = resource.getMessage(queryUserInfo().getLocale(), MessageType.errorcode, String.valueOf(errorCode.getCode()));
        return errorCode.getMessage(message);
    }
}