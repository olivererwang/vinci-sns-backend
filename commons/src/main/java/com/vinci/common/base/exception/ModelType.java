package com.vinci.common.base.exception;

/**
 * Created by tim@vinci on 15-1-28.
 */
public enum ModelType {
    system(1000000),
    user(1010000);

    private final int typeCode;
    private ModelType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
