package com.abdecd.moebackend.business.service.user;

import com.abdecd.moebackend.business.pojo.dto.user.*;
import com.abdecd.tokenlogin.pojo.entity.User;
import jakarta.annotation.Nonnull;

public interface UserService {
    User login(LoginDTO loginDTO);

    User loginByEmail(LoginByEmailDTO loginByEmailDTO);

    String generateUserToken(@Nonnull User user);

    User signup(SignUpDTO signUpDTO);

    void forgetPassword(ResetPwdDTO resetPwdDTO);

    String refreshUserToken();

    void deleteAccount(Long userId, String verifyCode);

    void changeEmail(Long userId, ChangeEmailDTO changeEmailDTO);
}
