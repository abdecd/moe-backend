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
    public static final String VERIFY_CODE_PREFIX = "moe:verify_code:";
    public static final String EMAIL_VERIFY_CODE_PREFIX = "moe:email_verify_code:";
    public static final String LIMIT_GET_STS = "moe:limit_get_sts:";
    public static final int LIMIT_GET_STS_CNT = 10;
    public static final int LIMIT_GET_STS_RESET_TIME = 5; // minutes
    public static final String VIDEO_GROUP_CACHE = "moe:video_group_cache#300:"; // minutes
    public static final String VIDEO_GROUP_CONTENT_CACHE = "moe:video_group_content_cache#300:"; // minutes
    public static final String VIDEO_GROUP_TYPE_CACHE = "moe:video_group_type_cache#300:";
    public static final String VIDEO_GROUP_PAGE_CACHE = "moe:video_group_page_cache#300:";
    public static final String VIDEO_LIST_CACHE = "moe:video_list_cache#300:";
    public static final String BANFUMI_VIDEO_GROUP_CACHE = "moe:bangumi_video_group_cache#300:"; // minutes
}
