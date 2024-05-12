package com.abdecd.moebackend.business.service.videogroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupAndTagMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupTagMapper;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.PlainVideoGroupVO;
import com.abdecd.moebackend.business.service.PlainUserService;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class PlainVideoGroupServiceBase {
    @Autowired
    private VideoGroupMapper videoGroupMapper;
    @Autowired
    private PlainUserService plainUserService;
    @Autowired
    private VideoGroupAndTagMapper videoGroupAndTagMapper;
    @Autowired
    private VideoGroupTagMapper videoGroupTagMapper;

    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_CACHE, key = "#videoGroupId", unless = "#result == null")
    public PlainVideoGroupVO getVideoGroupInfo(Long videoGroupId) {
        var base = videoGroupMapper.selectById(videoGroupId);
        if (base == null || !Objects.equals(base.getType(), VideoGroup.Type.PLAIN_VIDEO_GROUP)) return null;
        // 为空是管理员
        var uploader = plainUserService.getPlainUserDetail(base.getUserId());
        var uploaderVO = uploader == null ? null : new UploaderVO()
                .setId(uploader.getUserId())
                .setNickname(uploader.getNickname())
                .setAvatar(uploader.getAvatar());
        var tagIds = videoGroupAndTagMapper.selectList(new LambdaQueryWrapper<VideoGroupAndTag>()
                .select(VideoGroupAndTag::getTagId)
                .eq(VideoGroupAndTag::getVideoGroupId, videoGroupId)
        );
        if (tagIds == null) tagIds = new ArrayList<>();
        var tags = videoGroupTagMapper.selectBatchIds(tagIds.stream().map(VideoGroupAndTag::getTagId).toList());

        var vo = new PlainVideoGroupVO();
        BeanUtils.copyProperties(base, vo);
        vo.setUploader(uploaderVO);
        vo.setTags(tags);
        return vo;
    }
}
