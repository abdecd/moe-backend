package com.abdecd.moebackend.business.service.videogroup;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.mapper.BangumiVideoGroupMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupAndTagMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupTagMapper;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.BangumiVideoGroupVO;
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
public class BangumiVideoGroupServiceBase {
    @Autowired
    private VideoGroupMapper videoGroupMapper;
    @Autowired
    private BangumiVideoGroupMapper bangumiVideoGroupMapper;
    @Autowired
    private PlainUserService plainUserService;
    @Autowired
    private VideoGroupAndTagMapper videoGroupAndTagMapper;
    @Autowired
    private VideoGroupTagMapper videoGroupTagMapper;

    @Cacheable(cacheNames = RedisConstant.BANFUMI_VIDEO_GROUP_CACHE, key = "#videoGroupId", unless = "#result == null")
    public BangumiVideoGroupVO getVideoGroupInfo(Long videoGroupId) {
        var base = videoGroupMapper.selectById(videoGroupId);
        if (base == null || !Objects.equals(base.getType(), VideoGroup.Type.ANIME_VIDEO_GROUP)) return null;
        var appendix = bangumiVideoGroupMapper.selectOne(new LambdaQueryWrapper<BangumiVideoGroup>()
                .eq(BangumiVideoGroup::getVideoGroupId, videoGroupId)
        );
        if (appendix == null) return null;
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

        var vo = new BangumiVideoGroupVO();
        BeanUtils.copyProperties(base, vo);
        BeanUtils.copyProperties(appendix, vo);
        vo.setUploader(uploaderVO);
        vo.setTags(tags);
        return vo;
    }
}
