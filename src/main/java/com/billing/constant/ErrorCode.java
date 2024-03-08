package com.billing.constant;

public enum ErrorCode {
    
    DATA_ERROR_ESTIMATION("00001", "Data validation error for estimation."),
    DATA_ERROR_ITEM("00010", "Data validation error for item master."),
    DATA_ERROR_METAL_RATE("00010", "Data validation error for metal rate."),
    DATA_ERROR_CUSTOMER("00090", "Data validation error for Customer."),
    ;

    
    private final String code;
    private final String desc;

    ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
