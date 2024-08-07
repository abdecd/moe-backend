package com.abdecd.moebackend.common.constant;

public class RedisConstant {
    public static final String LIMIT_IP_RATE = "moe:limit:ip_rate:";
    public static final int LIMIT_IP_RATE_CNT = 200;
    public static final int LIMIT_IP_RATE_RESET_TIME = 3;
    public static final String LIMIT_VERIFY_EMAIL = "moe:limit:verify_email:";
    public static final int LIMIT_VERIFY_EMAIL_RESET_TIME = 60;
    public static final String LIMIT_UPLOAD_IMG = "moe:limit:upload_img:";
    public static final int LIMIT_UPLOAD_IMG_CNT = 300;
    public static final int LIMIT_UPLOAD_IMG_RESET_TIME = 1; // day
    public static final String VERIFY_CODE_PREFIX = "moe:verify_code:";
    public static final String EMAIL_VERIFY_CODE_PREFIX = "moe:email_verify_code:";
    public static final String LIMIT_GET_STS = "moe:limit:get_sts:";
    public static final int LIMIT_GET_STS_CNT = 10;
    public static final int LIMIT_GET_STS_RESET_TIME = 5; // minutes
    public static final String VIDEO_TRANSFORM_TASK_PREFIX = "moe:video_transform_task:";
    public static final String VIDEO_TRANSFORM_TASK_VIDEO_ID = "moe:video_transform_task_video_id:";
    public static final String VIDEO_TRANSFORM_TASK_CB_LOCK = "moe:video_transform_task_cb_lock:";

    public static final String VIDEO_VO = "moe:video_vo#d5";
    public static final String VIDEO_SRC = "moe:video_vo:src#d6";
    public static final String VIDEO_COMMENT = "moe:video_comment#300";
    public static final int VIDEO_COMMENT_CACHE_SIZE = 300;
    public static final String TIMESTAMP_VIDEO_COMMENT = "moe:timestamp:video_comment:";
    public static final String DANMAKU = "moe:danmaku";
    public static final String PLAIN_USER_DETAIL = "moe:plain_user:detail#300";
    public static final String PLAIN_USER_HISTORY = "moe:plain_user:history:";
    public static final String PLAIN_USER_HISTORY_LOCK = "moe:plain_user:history_lock:";
    public static final int PLAIN_USER_HISTORY_SIZE = 300;
    public static final String STATISTIC_VIDEO_PLAY_LOCK = "moe:statistic:video_play_statistic_lock:";
    public static final int STATISTIC_VIDEO_PLAY_RESET_TIME = 5;
    public static final String PLAIN_USER_LAST_WATCH_TIME = "moe:plain_user:last_watch_time:";
    public static final String PLAIN_USER_TOTAL_WATCH_TIME = "moe:plain_user:total_watch_time:";
    public static final String VIDEO_GROUP_WATCH_CNT = "moe:video_group:watch_cnt:";
    public static final String VIDEO_GROUP_LIKE_CNT = "moe:video_group:like_cnt:";
    public static final String VIDEO_GROUP_FAVORITE_CNT = "moe:video_group:favorite_cnt:";
    public static final String VIDEO_GROUP_CACHE = "moe:video_group_vo:video_group_cache";
    public static final String VIDEO_GROUP_TYPE_CACHE = "moe:video_group_type_cache#300";
    public static final String BANGUMI_VIDEO_GROUP_CACHE = "moe:video_group_vo:bangumi_video_group_cache";
    public static final String BANGUMI_VIDEO_GROUP_CONTENTS_CACHE = "moe:bangumi_video_group_contents_cache#300";
    public static final String VIDEO_GROUP_CONTENTS_CACHE = "moe:video_group_contents_cache#300";
    public static final String RECOMMEND_CAROUSEL = "moe:recommend_carousel";
    public static final String BANGUMI_TIME_SCHEDULE_CACHE = "moe:bangumi_time_schedule_cache#300";
    public static final String FAVORITE_PLAIN = "moe:plain_user:favorite:plain#300";
    public static final String FAVORITE_BANGUMI = "moe:plain_user:favorite:bangumi#300";
    public static final String IS_USER_FAVORITE = "moe:plain_user:favorite:is_user_favorite#300";
    public static final int FAVORITES_SIZE = 5000;
    public static final String IS_USER_LIKE = "moe:plain_user:like:is_user_like#300";
    public static final String VIDEO_COMMENT_CNT = "moe:video_comment_cnt#300";
    public static final String VIDEO_DANMAKU_CNT = "moe:video_danmaku_cnt";
    public static final String LIMIT_FEEDBACK_ADD = "moe:limit:feedback_add:";
    public static final int LIMIT_FEEDBACK_ADD_CNT = 10;
    public static final int LIMIT_FEEDBACK_ADD_RESET_TIME = 3; // seconds
    public static final String BANGUMI_INDEX_HOT = "moe:bangumi_index:hot";
    public static final long BANGUMI_INDEX_HOT_RESET_TIME = 7; // days
    public static final String BANGUMI_INDEX_FAVORITE_CNT = "moe:bangumi_index:favorite_cnt";
    public static final String BANGUMI_INDEX_FAVORITE_CNT_LOCK = "moe:bangumi_index:favorite_cnt_lock";
    public static final String BANGUMI_INDEX_WATCH_CNT = "moe:bangumi_index:watch_cnt";
    public static final String BANGUMI_INDEX_IDS = "moe:bangumi_index:ids#20";
    public static final String BILI_PARSER_BV = "moe:bili_parser:bv#3000";
    public static final String STATISTIC_VIDEO_PLAY_START_LOCK = "moe:statistic:video_play_start_statistic_lock:";
    public static final int STATISTIC_VIDEO_PLAY_START_RESET_TIME = 1;
    public static final String TIMESTAMP_DANMAKU = "moe:timestamp:danmaku:";
    public static final String LIMIT_DANMAKU_USER_MODIFY = "moe:limit:danmaku_user_modify:";
    public static final int LIMIT_DANMAKU_USER_MODIFY_RESET_TIME = 5; // seconds
    public static final String LIMIT_TRANSFORM_VIDEO = "moe:limit:transform_video";
    public static final String ANNOUNCEMENT = "moe:announcement";
}
