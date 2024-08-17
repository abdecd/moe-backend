package com.abdecd.moebackend.business.tokenLogin.common.util;

import com.abdecd.moebackend.business.tokenLogin.common.TokenLoginConstant;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

public class JwtUtils {
    private static final Algorithm HMAC256 = Algorithm.HMAC256(UUID.randomUUID() + "");

    public static String encodeJWT(String userId, String permission, int ttlSeconds) {
        var expiredDate = Calendar.getInstance();
        expiredDate.add(Calendar.SECOND, ttlSeconds);
        return JWT.create()
            .withClaim(TokenLoginConstant.K_USER_ID, userId)
            .withClaim(TokenLoginConstant.K_PERMISSION, permission)
            .withExpiresAt(expiredDate.getTime())
            .sign(HMAC256);
    }

    public static Map<String, String> decodeJWT(String token) {
        try {
            var verifiedToken = JWT
                .require(HMAC256)
                .build()
                .verify(token);
            return Map.of(
                TokenLoginConstant.K_USER_ID, verifiedToken.getClaim(TokenLoginConstant.K_USER_ID).asString(),
                TokenLoginConstant.K_PERMISSION, verifiedToken.getClaim(TokenLoginConstant.K_PERMISSION).asString(),
                TokenLoginConstant.K_EXPIRE, verifiedToken.getExpiresAt().getTime() + ""
            );
        } catch (Exception e) {
            return null;
        }
    }
}