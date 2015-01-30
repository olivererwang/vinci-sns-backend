package com.vinci.common.base.exception;

import java.io.Serializable;
import java.util.Locale;

public class ErrorCode implements Serializable {

    private static final long serialVersionUID = -6239192959362321352L;



    public ErrorCode(ModelType type , ErrorType errorType , int code, String message) {
        super();
        if (type == null) {
            type = ModelType.system;
        }
        if (errorType == null) {
            errorType = ErrorType.unknowErrorType;
        }
        if (code >= 10000) {
            code = 0;
        }
        this.code = type.getTypeCode() + errorType.getTypeCode() +code;
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

    public String getLocaleMessage(Locale lang) {
        return null;
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

}
