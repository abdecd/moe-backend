package com.abdecd.moebackend.business.service.PlainUserManage;

import com.abdecd.moebackend.business.dao.entity.UserManage;
import com.abdecd.moebackend.business.dao.mapper.PlainUserManageMapper;
import com.abdecd.moebackend.business.pojo.dto.plainuser.BanUserDTO;
import com.abdecd.moebackend.business.pojo.vo.plainuser.AllVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlainUserManageServierImpl implements PlainUserManageService {

    @Autowired
    private PlainUserManageMapper userMapper;

    @Override
    public Page<AllVO> listUsers(Long id, String name, Integer status, int page, int pageSize) {
        Page<UserManage> userPage = new Page<>(page, pageSize);
        Page<AllVO> voPage = userMapper.selectUsers(userPage, id, name, status);
        return voPage;
    }

    @Override
    public boolean banUser(BanUserDTO banUserDTO) {
        UserManage user = userMapper.selectById(banUserDTO.getId());
        if (user == null) {
            return false;
        }
        user.setStatus(banUserDTO.getStatus());
        return userMapper.updateById(user) == 1;
    }
}
