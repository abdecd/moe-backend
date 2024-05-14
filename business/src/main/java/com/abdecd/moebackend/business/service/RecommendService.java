package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// todo
@Service
public class RecommendService {
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;
    @Autowired
    private VideoGroupMapper videoGroupMapper;

    @Cacheable(cacheNames = RedisConstant.RECOMMEND_CAROUSEL_CACHE, unless = "#result == null")
    public List<VideoGroupWithDataVO> getCarousel() {
        var ids = videoGroupMapper.selectList(new LambdaQueryWrapper<VideoGroup>()
                .select(VideoGroup::getId)
                .last("order by RAND() limit 5")
        );
        if (ids.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(ids.stream()
                .map(id -> videoGroupServiceBase.getVideoGroupWithData(id.getId()))
                .toList()
        );
    }

    public List<VideoGroupWithDataVO> getRecommend(int num) {
        var ids = videoGroupMapper.selectList(new LambdaQueryWrapper<VideoGroup>()
                .select(VideoGroup::getId)
                .last("order by RAND() limit " + num)
        );
        if (ids.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(ids.stream()
                .map(id -> videoGroupServiceBase.getVideoGroupWithData(id.getId()))
                .toList()
        );
    }

    public List<VideoGroupWithDataVO> getRelated(Long videoGroupId, int num) {
        var ids = videoGroupMapper.selectList(new LambdaQueryWrapper<VideoGroup>()
                .select(VideoGroup::getId)
                .last("order by RAND() limit " + num)
        );
        if (ids.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(ids.stream()
                .map(id -> videoGroupServiceBase.getVideoGroupWithData(id.getId()))
                .toList()
        );
    }
}
