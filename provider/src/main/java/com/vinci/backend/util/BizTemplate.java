package com.vinci.backend.util;

import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;
import com.vinci.common.base.monitor.QMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BizTemplate
 */
public abstract class BizTemplate<T> {
    protected String monitorKey;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected BizTemplate(String monitorKey) {
        this.monitorKey = monitorKey;
    }

    protected abstract void checkParams() throws BizException;

    protected abstract T process() throws Exception;

    protected void afterProcess() {
    }

    protected void onSuccess() {
    }

    protected void onError(Throwable e) {
    }

    public T execute() throws BizException {
        try {
            checkParams();
        } catch (BizException e) {
            recordInvalidParam(e);
            throw e;
        } catch (Throwable e) {
            recordInvalidParam(e);
            throw new BizException(e, new ErrorCode(ErrorType.ArgumentErrorType, 0, e.getMessage()));
        }

        long start = System.currentTimeMillis();
        try {
            T result = process();
            onSuccess();
            QMonitor.recordOne(monitorKey + "_Success", System.currentTimeMillis() - start);
            return result;
        } catch (BizException e) {
            onError(e);
            ErrorCode code = e.getErrorCode();
            if (code == null) {
                code = new ErrorCode(ErrorType.unknownErrorType, 0, e.getMessage());
            }
            QMonitor.recordOne(monitorKey + "_BizException");
            QMonitor.recordOne(monitorKey + "_Failed");
            QMonitor.recordOne("BizTemplate_BizException");

            if (code.isCritical()) {
                QMonitor.recordOne(monitorKey + "_CriticalError");
                logger.warn("执行业务逻辑出现异常错误: monitorKey={}, code={}, msg={}", monitorKey, code, e.getMessage(), e);
            } else {
                QMonitor.recordOne(monitorKey + "_CommonError");
                logger.warn("执行业务逻辑出现普通错误: monitorKey={}, code={}, msg={}", monitorKey, code, e.getMessage());
            }
            throw e;
        } catch (Throwable e) {
            onError(e);
            logger.error("执行业务逻辑出现未知异常 monitoryKey={}", monitorKey, e);
            QMonitor.recordOne(monitorKey + "_UnknownError");
            QMonitor.recordOne(monitorKey + "_Failed");
            QMonitor.recordOne(monitorKey + "_CriticalError");
            QMonitor.recordOne("BizTemplate_UnknownError");
            throw new BizException(e, new ErrorCode(ErrorType.unknownErrorType, 0, e.getMessage()));

        } finally {
            afterProcess();
            QMonitor.recordOne(monitorKey + "_Invoke", System.currentTimeMillis() - start);
        }
    }

    private void recordInvalidParam(Throwable e) {
        if (logger.isDebugEnabled()) {
            logger.debug(monitorKey + "_校验参数失败", e);
        } else {
            logger.info(monitorKey + "_校验参数失败: " + e.getMessage());
        }
        QMonitor.recordOne(this.monitorKey + "_Invalid_Parameter");
        QMonitor.recordOne("BizTemplate_BizException_Invalid_Parameter");
    }
}
