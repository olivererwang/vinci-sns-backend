package com.vinci.common.base.exception;

/**
 * Created by tim@vinci on 15-1-28.
 */
public enum ModelType {
    system(100000000),
    user(110000000);
    private int i = Integer.MAX_VALUE;

    private final int typeCode;
    private ModelType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
