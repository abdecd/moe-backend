package com.abdecd.moebackend.common.constant;

public class DTOConstant {
    public static final int PERSON_NAME_LENGTH_MAX = 40;
    public static final int PERSON_NAME_LENGTH_MIN = 1;
    public static final int PASSWORD_LENGTH_MAX = 344;
    public static final int STRING_LENGTH_MAX = 200;
    public static final int COMMENT_LENGTH_MAX = 4000;
    public static final int FEEDBACK_LENGTH_MAX = 10000;
    public static final int CAPTCHA_LENGTH = 4;
    public static final int EMAIL_VERIFY_CODE_LENGTH = 6;
    public static final String TAGS_REGEXP = "^[^;]+(?:;[^;]+)*$";
    public static final String PERSON_NAME_REGEX = "^[^@ ]+$";
}
