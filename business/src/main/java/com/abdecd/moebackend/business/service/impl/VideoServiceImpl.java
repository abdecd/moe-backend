package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.mapper.VideoMapper;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.common.constant.RedisConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;

    @Override
    @Cacheable(cacheNames = RedisConstant.VIDEO_LIST_CACHE,key = "#id")
    public ArrayList<Video> getVideoListByGid(Integer videoGroupId) {
        return videoMapper.selectByGid(videoGroupId);
    }
}
