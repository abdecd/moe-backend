package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupAndTagMapper;
import com.abdecd.moebackend.business.service.VIdeoGroupAndTagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoGroupAndTagServiceImpl implements VIdeoGroupAndTagService {
    @Resource
    private VideoGroupAndTagMapper videoGroupAndTagMapper;

    @Override
    public void insert(Long tagId,Long groupId) {
        videoGroupAndTagMapper.insert(
                new VideoGroupAndTag()
                .setVideo_group_id(groupId)
                .setTag_id(tagId)
        );
    }
}
