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
    public static final String VIDEO_TRANSFORM_TASK_PREFIX = "moe:video_transform_task:";
    public static final String VIDEO_TRANSFORM_TASK_VIDEO_ID = "moe:video_transform_task_video_id:";
    public static final String VIDEO_TRANSFORM_TASK_CB_LOCK = "moe:video_transform_task_cb_lock:";

    public static final String VIDEO_VO = "moe:video_vo#300";
    public static final String VIDEO_COMMENT = "moe:video_comment#300";
    public static final int VIDEO_COMMENT_CACHE_SIZE = 300;
    public static final String VIDEO_COMMENT_TIMESTAMP = "moe:timestamp:video_comment:";
    public static final String DANMAKU = "moe:danmaku#20";
    public static final String PLAIN_USER_DETAIL = "moe:plain_user:detail#300";
    public static final String PLAIN_USER_HISTORY = "moe:plain_user:history:";
    public static final int PLAIN_USER_HISTORY_SIZE = 300;
    public static final String STATISTIC_VIDEO_PLAY_LOCK = "moe:statistic:video_play_statistic_lock:";
    public static final int STATISTIC_VIDEO_PLAY_RESET_TIME = 5;
    public static final String PLAIN_USER_LAST_WATCH_TIME = "moe:plain_user:last_watch_time:";
    public static final String PLAIN_USER_TOTAL_WATCH_TIME = "moe:plain_user:total_watch_time:";
    public static final String STATISTIC_VIDEO_PLAY_CNT = "moe:statistic:video_play_cnt:";
    public static final String VIDEO_GROUP_CACHE = "moe:video_group_cache#300";
    public static final String VIDEO_GROUP_CONTENT_CACHE = "moe:video_group_content_cache#300";
    public static final String VIDEO_GROUP_TYPE_CACHE = "moe:video_group_type_cache#300";
    public static final String VIDEO_LIST_CACHE = "moe:video_list_cache#300";
    public static final String BANGUMI_VIDEO_GROUP_CACHE = "moe:bangumi_video_group_cache#300";
    public static final String BANGUMI_VIDEO_GROUP_CONTENTS_CACHE = "moe:bangumi_video_group_cache#300";
    public static final String VIDEO_GROUP_CONTENTS_CACHE = "moe:video_group_contents_cache#300";
    public static final String RECOMMEND_CAROUSEL_CACHE = "moe:recommend_carousel_cache#d1";
    public static final String BANGUMI_TIME_SCHEDULE_CACHE = "moe:bangumi_time_schedule_cache#30000";
}
