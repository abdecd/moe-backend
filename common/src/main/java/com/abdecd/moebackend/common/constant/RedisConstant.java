package com.abdecd.moebackend.common.constant;

public interface RedisConstant {
    String LIMIT_IP_RATE = "moe:limit:ip_rate:";
    int LIMIT_IP_RATE_CNT = 200;
    int LIMIT_IP_RATE_RESET_TIME = 3;
    String LIMIT_VERIFY_EMAIL = "moe:limit:verify_email:";
    int LIMIT_VERIFY_EMAIL_RESET_TIME = 60;
    String LIMIT_UPLOAD_IMG = "moe:limit:upload_img:";
    int LIMIT_UPLOAD_IMG_CNT = 300;
    int LIMIT_UPLOAD_IMG_RESET_TIME = 1; // day
    String VERIFY_CODE_PREFIX = "moe:verify_code:";
    String EMAIL_VERIFY_CODE_PREFIX = "moe:email_verify_code:";
    String LIMIT_GET_STS = "moe:limit:get_sts:";
    int LIMIT_GET_STS_CNT = 10;
    int LIMIT_GET_STS_RESET_TIME = 5; // minutes
    String VIDEO_TRANSFORM_TASK_PREFIX = "moe:video_transform_task:";
    String VIDEO_TRANSFORM_TASK_VIDEO_ID = "moe:video_transform_task_video_id:";
    String VIDEO_TRANSFORM_TASK_CB_LOCK = "moe:video_transform_task_cb_lock:";

    String VIDEO_VO = "moe:video_vo#d5";
    String VIDEO_SRC = "moe:video_vo:src#d6";
    String VIDEO_COMMENT = "moe:video_comment#300";
    int VIDEO_COMMENT_CACHE_SIZE = 300;
    String TIMESTAMP_VIDEO_COMMENT = "moe:timestamp:video_comment:";
    String DANMAKU = "moe:danmaku";
    String PLAIN_USER_DETAIL = "moe:plain_user:detail#300";
    String PLAIN_USER_HISTORY = "moe:plain_user:history:";
    String PLAIN_USER_HISTORY_LOCK = "moe:plain_user:history_lock:";
    int PLAIN_USER_HISTORY_SIZE = 300;
    String STATISTIC_VIDEO_PLAY_LOCK = "moe:statistic:video_play_statistic_lock:";
    int STATISTIC_VIDEO_PLAY_RESET_TIME = 5;
    String PLAIN_USER_LAST_WATCH_TIME = "moe:plain_user:last_watch_time:";
    String PLAIN_USER_TOTAL_WATCH_TIME = "moe:plain_user:total_watch_time:";
    String VIDEO_GROUP_WATCH_CNT = "moe:video_group:watch_cnt:";
    String VIDEO_GROUP_LIKE_CNT = "moe:video_group:like_cnt:";
    String VIDEO_GROUP_FAVORITE_CNT = "moe:video_group:favorite_cnt:";
    String VIDEO_GROUP_CACHE = "moe:video_group_vo:video_group_cache";
    String VIDEO_GROUP_TYPE_CACHE = "moe:video_group_type_cache#300";
    String BANGUMI_VIDEO_GROUP_CACHE = "moe:video_group_vo:bangumi_video_group_cache";
    String BANGUMI_VIDEO_GROUP_CONTENTS_CACHE = "moe:bangumi_video_group_contents_cache#300";
    String VIDEO_GROUP_CONTENTS_CACHE = "moe:video_group_contents_cache#300";
    String RECOMMEND_CAROUSEL = "moe:recommend_carousel";
    String BANGUMI_TIME_SCHEDULE_CACHE = "moe:bangumi_time_schedule_cache#300";
    String FAVORITE_PLAIN = "moe:plain_user:favorite:plain#300";
    String FAVORITE_BANGUMI = "moe:plain_user:favorite:bangumi#300";
    String IS_USER_FAVORITE = "moe:plain_user:favorite:is_user_favorite#300";
    int FAVORITES_SIZE = 5000;
    String IS_USER_LIKE = "moe:plain_user:like:is_user_like#300";
    String VIDEO_COMMENT_CNT = "moe:video_comment_cnt#300";
    String VIDEO_DANMAKU_CNT = "moe:video_danmaku_cnt";
    String LIMIT_FEEDBACK_ADD = "moe:limit:feedback_add:";
    int LIMIT_FEEDBACK_ADD_CNT = 10;
    int LIMIT_FEEDBACK_ADD_RESET_TIME = 3; // seconds
    String BANGUMI_INDEX_HOT = "moe:bangumi_index:hot";
    long BANGUMI_INDEX_HOT_RESET_TIME = 7; // days
    String BANGUMI_INDEX_FAVORITE_CNT = "moe:bangumi_index:favorite_cnt";
    String BANGUMI_INDEX_FAVORITE_CNT_LOCK = "moe:bangumi_index:favorite_cnt_lock";
    String BANGUMI_INDEX_WATCH_CNT = "moe:bangumi_index:watch_cnt";
    String BANGUMI_INDEX_IDS = "moe:bangumi_index:ids#20";
    String BILI_PARSER_BV = "moe:bili_parser:bv#3000";
    String STATISTIC_VIDEO_PLAY_START_LOCK = "moe:statistic:video_play_start_statistic_lock:";
    int STATISTIC_VIDEO_PLAY_START_RESET_TIME = 1;
    String TIMESTAMP_DANMAKU = "moe:timestamp:danmaku:";
    String LIMIT_DANMAKU_USER_MODIFY = "moe:limit:danmaku_user_modify:";
    int LIMIT_DANMAKU_USER_MODIFY_RESET_TIME = 5; // seconds
    String LIMIT_TRANSFORM_VIDEO = "moe:limit:transform_video";
    String ANNOUNCEMENT = "moe:announcement";
    String LIMIT_LOGIN = "moe:limit:login_cnt:";
    String USER = "moe:user";
    String USER_EMAIL_KEY = "moe:user:email_key";
}
