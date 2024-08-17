package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.dataencrypt.EncryptStrHandler;
import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.dao.entity.User;
import com.abdecd.moebackend.business.dao.mapper.PlainUserDetailMapper;
import com.abdecd.moebackend.business.dao.service.UserService;
import com.abdecd.moebackend.business.pojo.dto.user.*;
import com.abdecd.moebackend.business.service.common.CommonService;
import com.abdecd.moebackend.business.tokenLogin.service.TokenLoginService;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class LoginService {
    @Autowired
    UserService userService;
    @Autowired
    TokenLoginService tokenLoginService;
    @Autowired
    CommonService commonService;
    @Autowired
    PlainUserDetailMapper plainUserDetailMapper;

    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User willLoginUser = null;
        try {
            willLoginUser = userService.getUserById(Long.parseLong(username));
        } catch (NumberFormatException ignored) {
        }
        if (willLoginUser == null) {
            willLoginUser = userService.getUserByEmail(username);
        }

        // 进行登录
        var user = willLoginUser;
        // 用户不存在
        if (user == null) return null;
        // 用户被删除
        if (Objects.equals(user.getStatus(), User.Status.DELETED)) return null;
        if (Objects.equals(user.getStatus(), User.Status.LOCKED)) {
            //账号被锁定
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }
        String hashPwd;
        try {
            hashPwd = tokenLoginService.convertPwd(password, user.getId().toString());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        if (!hashPwd.equals(user.getPassword())) {
            //密码错误
            throw new BaseException(MessageConstant.LOGIN_PASSWORD_ERROR);
        }
        return user;
    }

    public User loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        // 登录
        var user = userService.getUserByEmail(loginByEmailDTO.getEmail());

        // 用户不存在
        if (user == null) return null;
        // 用户被删除
        if (Objects.equals(user.getStatus(), User.Status.DELETED)) return null;
        if (Objects.equals(user.getStatus(), User.Status.LOCKED)) {
            //账号被锁定
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }
        return user;
    }

    @Transactional
    public User signup(SignUpDTO signUpDTO) {
        // 检验重复
        if (userService.exists(new LambdaQueryWrapper<User>()
            .eq(User::getEmail, EncryptStrHandler.encrypt(signUpDTO.getEmail()))
            .or().eq(User::getNickname, signUpDTO.getNickname())
        )) throw new BaseException(MessageConstant.USER_DULPLICATE);
        // 注册
        var user = User.ofEmpty()
            .setPermission("1")
            .setNickname(signUpDTO.getNickname())
            .setEmail(signUpDTO.getEmail());
        userService.save(user);
        try {
            userService.updateById(user
                .setPassword(tokenLoginService.convertPwd(signUpDTO.getPassword(), user.getId().toString()))
            );
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        plainUserDetailMapper.insert(new PlainUserDetail()
            .setUserId(user.getId())
            .setNickname(signUpDTO.getNickname())
            .setAvatar("")
            .setSignature("")
        );
        return user;
    }

    public void forgetPassword(ResetPwdDTO resetPwdDTO) {
        // 重置密码
        var user = userService.getOne(new LambdaQueryWrapper<User>()
            .eq(User::getEmail, EncryptStrHandler.encrypt(resetPwdDTO.getEmail()))
        );
        if (user == null) throw new BaseException(MessageConstant.USER_NOT_EXIST);
        try {
            userService.updateById(new User()
                .setId(user.getId())
                .setPassword(tokenLoginService.convertPwd(resetPwdDTO.getNewPassword(), user.getId().toString()))
            );
            // 删除缓存
            userService.getUserCache().delete(user.getId() + "");
            tokenLoginService.forceLogout(user.getId());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    public void deleteAccount(Long userId, String verifyCode) {
        var user = userService.getById(userId);
        if (user == null) throw new BaseException(MessageConstant.USER_NOT_EXIST);
        if (Objects.equals(user.getStatus(), User.Status.LOCKED))
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        // 验证邮箱
        commonService.verifyEmail(user.getEmail(), verifyCode);
        // 删除账号
        userService.updateById(User.toBeDeleted(user.getId()));
        // 删除缓存
        userService.getUserCache().delete(user.getId() + "");
        userService.getUserEmailKeyCache().delete(user.getEmail());
        tokenLoginService.forceLogout(user.getId());
    }

    public void changeEmail(Long userId, ChangeEmailDTO changeEmailDTO) {
        var user = userService.getById(userId);
        if (user == null) throw new BaseException(MessageConstant.USER_NOT_EXIST);
        var oldEmail = user.getEmail();
        // 检验重复
        if (userService.exists(new LambdaQueryWrapper<User>()
            .eq(User::getEmail, EncryptStrHandler.encrypt(changeEmailDTO.getNewEmail()))
        )) throw new BaseException(MessageConstant.USER_DULPLICATE);
        userService.updateById(user.setEmail(changeEmailDTO.getNewEmail()));
        // 删除缓存
        userService.getUserCache().delete(user.getId() + "");
        userService.getUserEmailKeyCache().delete(oldEmail);
    }
}
