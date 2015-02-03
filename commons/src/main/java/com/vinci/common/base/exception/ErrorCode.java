package com.vinci.common.base.exception;

import java.io.Serializable;
import java.util.Locale;

public class ErrorCode implements Serializable {

    private static final long serialVersionUID = -6239192959362321352L;

    private ErrorType errorType;

    public ErrorCode(ErrorType errorType , int code, String message) {
        super();
        if (errorType == null) {
            this.errorType = ErrorType.unknownErrorType;
        } else {
            this.errorType = errorType;
        }
        if (code >= 10000) {
            code = 0;
        }
        this.code = this.errorType.getTypeCode() +code;
        this.message = message;
    }
    
    public ErrorCode(){
        
    }

    public ErrorCode copy(String... extraMessage) {
        ErrorCode errorCode = new ErrorCode(this.errorType, this.code - this.errorType.getTypeCode(), message);
        if (extraMessage != null && extraMessage.length > 0) {
            errorCode.setExtraMessage(extraMessage);
        }
        return errorCode;
    }

    private int code;
    private String message;
    private String[] extraMessage;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return getMessage(null);
    }

    public String getMessage(String localeMessage) {
        String message = (localeMessage == null ? this.message : localeMessage);
        if (extraMessage == null) {
            return message;
        }
        return String.format(message, extraMessage);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String[] getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String[] extraMessage) {
        this.extraMessage = extraMessage;
    }

    @Override
    public String toString() {
        return "ErrorCode [code=" + code + ", message=" + getMessage() + "]";
    }

    public boolean isCritical() {
        return (getErrorType() == ErrorType.unknownErrorType || getErrorType() == ErrorType.databaseErrorType);
    }

}
