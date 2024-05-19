package com.abdecd.moebackend.business.service.backstage.impl;

import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupAndTagMapper;
import com.abdecd.moebackend.business.service.backstage.VideoGroupAndTagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoGroupAndTagServiceImpl implements VideoGroupAndTagService {
    @Resource
    private VideoGroupAndTagMapper videoGroupAndTagMapper;

    @Override
    public void insert(Long tagId,Long groupId) {
        videoGroupAndTagMapper.insert(
                new VideoGroupAndTag()
                .setVideoGroupId(groupId)
                .setTagId(tagId)
        );
    }

    @Override
    public void insertByTags(String tags, Long groupId) {
        String[] tagIds = tags.split(",");

        for (String tagId : tagIds) {
            videoGroupAndTagMapper.insert(
                    new VideoGroupAndTag()
                        .setVideoGroupId(groupId)
                        .setTagId(Long.valueOf(tagId))
            );
        }
    }

    @Override
    public void deleteByVideoGroupId(Long id) {
        videoGroupAndTagMapper.deleteByVideoGroupId(id);
    }
}
