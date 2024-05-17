package com.abdecd.moebackend.common.constant;

public class DTOConstant {
    public static final int PERSON_NAME_LENGTH_MAX = 40;
    public static final int PERSON_NAME_LENGTH_MIN = 1;
    public static final int PASSWORD_LENGTH_MAX = 344;
    public static final int STRING_LENGTH_MAX = 200;
    public static final int COMMENT_LENGTH_MAX = 1000;
    public static final int CAPTCHA_LENGTH = 4;
    public static final int EMAIL_VERIFY_CODE_LENGTH = 6;
    public static final String TAGS_REGEXP = "^[A-Za-z0-9\\u4e00-\\u9fa5]+(?:;[A-Za-z0-9\\u4e00-\\u9fa5]+)*$";
}
