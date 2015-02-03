package com.vinci.common.base.exception;

/**
 * 错误类型
 * Created by tim@vinci on 15-1-29.
 */
public enum ErrorType {
    /** 参数错误 **/
    ArgumentErrorType(1010000),
    /** 数据库相关错误 **/
    databaseErrorType(1020000),
    /** 数据约束(冲突)相关错误，例如唯一索引约束等**/
    dataConventionErrorType(1030000),
    WebArgumentErrorType(1050000),
    unknownErrorType(9990000);

    private final int typeCode;

    private ErrorType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

}
