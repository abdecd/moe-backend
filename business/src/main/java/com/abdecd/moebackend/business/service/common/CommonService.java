package com.abdecd.moebackend.business.service.common;

import org.springframework.data.util.Pair;

public interface CommonService {
    Pair<String, byte[]> generateCaptcha();

    void verifyCaptcha(String uuid, String captcha);

    void sendCodeToVerifyEmail(String email);

    void verifyEmail(String email, String code);
}
