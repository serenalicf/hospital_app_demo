package com.serena.commonutil.result;

import lombok.Getter;

/**
 * Unified return result status information class
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "Success"),
    FAIL(201, "Failed"),
    PARAM_ERROR(202, "Incorrect parameters"),
    SERVICE_ERROR(203, "Service exception"),
    DATA_ERROR(204, "Data exception"),
    DATA_UPDATE_ERROR(205, "Data version exception"),

    LOGIN_AUTH(208, "Not logged in"),
    PERMISSION(209, "No permission"),

    CODE_ERROR(210, "Verification code error"),
    //    LOGIN_MOBLE_ERROR(211, "Account incorrect"),
    LOGIN_DISABLED_ERROR(212, "This user has been disabled"),
    REGISTER_MOBLE_ERROR(213, "Phone number already in use"),
    LOGIN_AURH(214, "Login required"),
    LOGIN_ACL(215, "No permission"),

    URL_ENCODE_ERROR(216, "URL encoding failed"),
    ILLEGAL_CALLBACK_REQUEST_ERROR(217, "Illegal callback request"),
    FETCH_ACCESSTOKEN_FAILD(218, "Failed to get accessToken"),
    FETCH_USERINFO_ERROR(219, "Failed to get user information"),
    //LOGIN_ERROR(23005, "Login failed"),

    PAY_RUN(220, "Payment in progress"),
    CANCEL_ORDER_FAIL(225, "Failed to cancel order"),
    CANCEL_ORDER_NO(225, "Cannot cancel appointment"),

    HOSCODE_EXIST(230, "Hospital code already exists"),
    NUMBER_NO(240, "Insufficient appointment slots"),
    TIME_NO(250, "Current time is not available for appointment"),

    SIGN_ERROR(300, "Signature error"),
    HOSPITAL_OPEN(310, "Hospital not open, temporarily unavailable"),
    HOSPITAL_LOCK(320, "Hospital is locked, temporarily unavailable"),
    ;


    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
