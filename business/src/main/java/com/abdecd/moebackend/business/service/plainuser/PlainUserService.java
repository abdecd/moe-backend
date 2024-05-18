package com.abdecd.moebackend.business.service.plainuser;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.dao.mapper.PlainUserDetailMapper;
import com.abdecd.moebackend.business.pojo.dto.plainuser.UpdatePlainUserDTO;
import com.abdecd.moebackend.business.service.fileservice.FileService;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.abdecd.tokenlogin.mapper.UserMapper;
import com.abdecd.tokenlogin.pojo.entity.User;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
public class PlainUserService {
    @Autowired
    private PlainUserDetailMapper plainUserDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService fileService;

    @Cacheable(value = RedisConstant.PLAIN_USER_DETAIL, key = "#uid", unless = "#result == null")
    public PlainUserDetail getPlainUserDetail(Long uid) {
        return plainUserDetailMapper.selectById(uid);
    }

    @CacheEvict(value = RedisConstant.PLAIN_USER_DETAIL, key = "#root.target.getCurrUserId()")
    @Transactional
    public void updatePlainUserDetail(UpdatePlainUserDTO updatePlainUserDTO) {
        String newFileUrl = null;
        String oldFileUrl = null;
        if (updatePlainUserDTO.getAvatar() != null) {
            try {
                newFileUrl = fileService.uploadFile(updatePlainUserDTO.getAvatar(), "/user", "avatar" + UUID.randomUUID());
                oldFileUrl = plainUserDetailMapper.selectById(UserContext.getUserId()).getAvatar();
            } catch (IOException e) {
                throw new BaseException(MessageConstant.IMG_FILE_UPLOAD_FAIL);
            }
        }
        if (updatePlainUserDTO.getNickname() != null) {
            userMapper.update(new LambdaUpdateWrapper<User>()
                    .eq(User::getId, UserContext.getUserId())
                    .set(User::getNickname, updatePlainUserDTO.getNickname())
            );
        }
        var entity = updatePlainUserDTO.toEntity(UserContext.getUserId());
        if (newFileUrl != null) entity.setAvatar(newFileUrl);
        plainUserDetailMapper.updateById(entity);
        if (oldFileUrl != null) fileService.deleteFile(oldFileUrl);
    }

    @SuppressWarnings("unused")
    public Long getCurrUserId() {
        return UserContext.getUserId();
    }
}
