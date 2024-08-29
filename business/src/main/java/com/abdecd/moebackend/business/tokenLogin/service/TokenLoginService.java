package com.abdecd.moebackend.business.tokenLogin.service;

import com.abdecd.moebackend.business.tokenLogin.common.TokenLoginConstant;
import com.abdecd.moebackend.business.tokenLogin.common.TokenLoginProp;
import com.abdecd.moebackend.business.tokenLogin.common.UserContext;
import com.abdecd.moebackend.business.tokenLogin.common.util.JwtUtils;
import com.abdecd.moebackend.business.tokenLogin.common.util.PwdUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class TokenLoginService {
    @Resource
    private TokenLoginProp tokenLoginProp;
    @Resource
    private LoginBlackListService loginBlackListService;

    /**
     * 密码 hash
     * @param password 明文密码
     * @param saltKey 密码盐，两个都相同 hash 才会相同
     * @return hash后的密码
     * @throws Exception 密码强度不够等
     */
    public String convertPwd(String password, String saltKey) throws Exception {
        password = PwdUtils.decryptPwd(password);
        if (!TokenLoginConstant.PASSWORD_PATTERN.matcher(password).find()) {
            throw new Exception("密码格式错误");
        }
        return PwdUtils.encodePwd(saltKey, password);
    }

    public String generateUserToken(String userId, String permission) {
        return JwtUtils.encodeJWT(userId, permission, tokenLoginProp.getJwtTtlSeconds());
    }

    public String refreshUserToken() {
        return JwtUtils.encodeJWT(UserContext.getUserId() + "", UserContext.getPermission(), tokenLoginProp.getJwtTtlSeconds());
    }

    public void forceLogout(Long userId) {
        loginBlackListService.forceLogout(userId);
    }

    /**
     * 返回Base64编码的公钥 为pem格式去掉开头和结尾的-----BEGIN PUBLIC KEY-----和-----END PUBLIC KEY-----
     */
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(PwdUtils.publicKey.getEncoded());
    }
}
