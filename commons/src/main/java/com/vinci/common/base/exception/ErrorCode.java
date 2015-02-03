package com.vinci.common.base.exception;

import java.io.Serializable;

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

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorCode [code=");
        builder.append(code);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }

    public boolean isCritical() {
        return (getErrorType() == ErrorType.unknownErrorType || getErrorType() == ErrorType.databaseErrorType);
    }
}
