package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.mapper.PlainUserDetailMapper;
import com.abdecd.moebackend.business.pojo.dto.user.*;
import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.service.UserService;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.StatusConstant;
import com.abdecd.tokenlogin.common.dataencrypt.EncryptStrHandler;
import com.abdecd.tokenlogin.mapper.UserMapper;
import com.abdecd.tokenlogin.pojo.entity.User;
import com.abdecd.tokenlogin.service.UserBaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserBaseService userBaseService;
    @Autowired
    CommonServiceImpl commonService;
    @Autowired
    PlainUserDetailMapper plainUserDetailMapper;

    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User willLoginUser = null;
        try {
            willLoginUser = userMapper.selectById(Integer.parseInt(username));
        } catch (NumberFormatException ignored) {}
        if (willLoginUser == null) {
            willLoginUser = userMapper.selectOne(
                    new LambdaQueryWrapper<User>().eq(User::getEmail, EncryptStrHandler.encrypt(username))
            );
        }
        return userBaseService.login(
                willLoginUser,
                password,
                BaseException.class,
                MessageConstant.LOGIN_PASSWORD_ERROR,
                MessageConstant.ACCOUNT_LOCKED
        );
    }

    @Override
    public User loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        // 验证邮箱
        commonService.verifyEmail(loginByEmailDTO.getEmail(), loginByEmailDTO.getVerifyCode());
        // 登录
        var willLoginUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, EncryptStrHandler.encrypt(loginByEmailDTO.getEmail()))
        );
        return userBaseService.forceLogin(willLoginUser);
    }

    @Override
    public String generateUserToken(@Nonnull User user) {
        return userBaseService.generateUserToken(user);
    }

    @Transactional
    @Override
    public User signup(SignUpDTO signUpDTO) {
        // 验证邮箱
        commonService.verifyEmail(signUpDTO.getEmail(), signUpDTO.getVerifyCode());
        // 注册
        var user = userBaseService.signup(signUpDTO.getPassword(), "1", BaseException.class);
        if (user == null) throw new BaseException(MessageConstant.SIGNUP_FAILED);
        userMapper.updateById(user
                .setNickname(signUpDTO.getNickname())
                .setEmail(signUpDTO.getEmail())
        );
        plainUserDetailMapper.insert(new PlainUserDetail()
                .setUserId(user.getId())
                .setAvatar("")
                .setSignature("")
        );
        return user;
    }

    @Override
    public void forgetPassword(ResetPwdDTO resetPwdDTO) {
        // 验证邮箱
        commonService.verifyEmail(resetPwdDTO.getEmail(), resetPwdDTO.getVerifyCode());
        // 重置密码
        var user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, resetPwdDTO.getEmail())
        );
        userBaseService.forgetPassword(user.getId(), resetPwdDTO.getNewPassword(), BaseException.class);
    }

    @Override
    public String refreshUserToken() {
        return userBaseService.refreshUserToken();
    }

    @Override
    public void deleteAccount(Long userId, String verifyCode) {
        var user = userMapper.selectById(userId);
        if (Objects.equals(user.getStatus(), StatusConstant.DISABLE))
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        // 验证邮箱
        commonService.verifyEmail(user.getEmail(), verifyCode);
        // 删除账号
        userBaseService.deleteAccount(user);
    }

    @Override
    public void changeEmail(Long userId, ChangeEmailDTO changeEmailDTO) {
        var user = userMapper.selectById(userId);
        if (user == null) return;
        // 验证邮箱
        commonService.verifyEmail(changeEmailDTO.getNewEmail(), changeEmailDTO.getVerifyCode());
        userMapper.updateById(user.setEmail(changeEmailDTO.getNewEmail()));
    }
}
