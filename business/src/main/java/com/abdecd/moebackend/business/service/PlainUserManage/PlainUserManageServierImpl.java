package com.abdecd.moebackend.business.service.PlainUserManage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.User;
import com.abdecd.moebackend.business.dao.entity.UserManage;
import com.abdecd.moebackend.business.dao.mapper.PlainUserManageMapper;
import com.abdecd.moebackend.business.pojo.dto.plainuser.BanUserDTO;
import com.abdecd.moebackend.business.pojo.vo.plainuser.AllVO;
import com.abdecd.moebackend.business.tokenLogin.service.TokenLoginService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlainUserManageServierImpl implements PlainUserManageService {

    @Autowired
    private PlainUserManageMapper userMapper;
    @Autowired
    private TokenLoginService tokenLoginService;

    @Override
    public Page<AllVO> listUsers(Long id, String name, Integer status, int page, int pageSize) {
        Page<UserManage> userPage = new Page<>(page, pageSize);
        return userMapper.selectUsers(userPage, id, name, status);
    }

    @Override
    public boolean banUser(BanUserDTO banUserDTO) {
        UserManage user = userMapper.selectById(banUserDTO.getId());
        if (user == null) {
            return false;
        }
        if (banUserDTO.getStatus() == User.Status.LOCKED) {
            tokenLoginService.forceLogout(user.getId());
        }
        user.setStatus(banUserDTO.getStatus());
        return userMapper.updateById(user) == 1;
    }

    @Override
    public void updatePermission(Long id, String permission) {
        var user = userMapper.selectById(id);
        if (user == null) throw new BaseException("用户不存在");
        user.setPermission(permission);
        userMapper.updateById(user);
        tokenLoginService.forceLogout(user.getId());
    }
}
