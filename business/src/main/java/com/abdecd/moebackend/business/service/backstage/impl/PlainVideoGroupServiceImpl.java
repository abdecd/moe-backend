package com.abdecd.moebackend.business.service.backstage.impl;

import com.abdecd.moebackend.business.dao.mapper.PlainVideoGroupMapper;
import com.abdecd.moebackend.business.pojo.vo.videogroup.PlainVideoGroupVO;
import com.abdecd.moebackend.business.service.backstage.PlainVideoGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlainVideoGroupServiceImpl implements PlainVideoGroupService {
    @Autowired
    private PlainVideoGroupMapper plainVideoGroupMapper;

    @Override
    public ArrayList<PlainVideoGroupVO> getAllVideoGroup(Integer pageIndex, Integer pageSize, String id, String title, Byte status) {
        var list = plainVideoGroupMapper.selectVideoGroupList(pageIndex, pageSize, id, title, status);
        list.forEach(plainVideoGroupVO -> {
            if (plainVideoGroupVO.getUploader().getId() == 1) {
                plainVideoGroupVO.getUploader().setNickname("管理员");
            }
        });

        return list;
    }

    @Override
    public Integer countPlainVideoGroup(String id, String title, Byte status) {
        return plainVideoGroupMapper.selectVideoGroupListCount(id, title, status);
    }


}
