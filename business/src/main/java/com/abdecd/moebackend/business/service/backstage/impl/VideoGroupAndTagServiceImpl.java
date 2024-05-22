package com.abdecd.moebackend.business.service.backstage.impl;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupAndTagMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.service.backstage.VideoGroupAndTagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoGroupAndTagServiceImpl implements VideoGroupAndTagService {
    @Resource
    private VideoGroupAndTagMapper videoGroupAndTagMapper;
    @Autowired
    private VideoGroupMapper videoGroupMapper;

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

    @Override
    public void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO) {

        Integer updateID = videoGroupMapper.updateTagsByID(
                new VideoGroup()
                        .setId(bangumiVideoGroupUpdateDTO.getId())
                        .setTags(bangumiVideoGroupUpdateDTO.getTags())
        );

        if(updateID == 1){
            videoGroupAndTagMapper.deleteByVideoGroupId(bangumiVideoGroupUpdateDTO.getId());
            String[] tags = bangumiVideoGroupUpdateDTO.getTags().split(";");
            for(String tagid : tags) {
                videoGroupAndTagMapper.insert(
                        new VideoGroupAndTag()
                                .setTagId(Long.valueOf(tagid))
                                .setVideoGroupId(bangumiVideoGroupUpdateDTO.getId())
                );
            }
        }
    }
}
