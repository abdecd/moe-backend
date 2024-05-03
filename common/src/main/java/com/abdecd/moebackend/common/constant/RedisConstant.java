package com.abdecd.moebackend.common.constant;

public class RedisConstant {
    public static final String LIMIT_IP_RATE = "moe:limit_ip_rate:";
    public static final int LIMIT_IP_RATE_CNT = 200;
    public static final int LIMIT_IP_RATE_RESET_TIME = 3;
    public static final String LIMIT_VERIFY_EMAIL = "moe:limit_verify_email:";
    public static final int LIMIT_VERIFY_EMAIL_RESET_TIME = 60;
    public static final String LIMIT_UPLOAD_IMG = "moe:limit_upload_img:";
    public static final int LIMIT_UPLOAD_IMG_CNT = 300;
    public static final int LIMIT_UPLOAD_IMG_RESET_TIME = 1; // day
}
