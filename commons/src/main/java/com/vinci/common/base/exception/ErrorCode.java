package com.vinci.common.base.exception;

import com.vinci.common.base.i18n.I18NResource;

import java.io.Serializable;
import java.util.Locale;

public class ErrorCode implements Serializable {

    private static final long serialVersionUID = -6239192959362321352L;

    private ModelType modelType;

    private ErrorType errorType;

    public ErrorCode(ModelType type , ErrorType errorType , int code, String message) {
        super();
        if (type == null) {
            this.modelType = ModelType.system;
        } else {
            this.modelType = type;
        }
        if (errorType == null) {
            this.errorType = ErrorType.unknowErrorType;
        } else {
            this.errorType = errorType;
        }
        if (code >= 10000) {
            code = 0;
        }
        this.code = this.modelType.getTypeCode() + this.errorType.getTypeCode() +code;
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

    public ModelType getModelType() {
        return modelType;
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
        return (getErrorType() == ErrorType.unknowErrorType || getErrorType() == ErrorType.databaseErrorType);
    }
}
