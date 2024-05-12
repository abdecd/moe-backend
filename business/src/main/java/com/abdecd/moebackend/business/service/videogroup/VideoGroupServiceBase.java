package com.abdecd.moebackend.business.service.videogroup;

import com.abdecd.moebackend.business.common.util.SpringContextUtil;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupVO;
import com.abdecd.moebackend.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class VideoGroupServiceBase {
    @Autowired
    private VideoGroupMapper videoGroupMapper;
    @Autowired
    private PlainVideoGroupServiceBase plainVideoGroupServiceBase;
    @Autowired
    private BangumiVideoGroupServiceBase bangumiVideoGroupServiceBase;

    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_TYPE_CACHE, key = "#videoGroupId")
    public Byte getVideoGroupType(Long videoGroupId) {
        var obj = videoGroupMapper.selectById(videoGroupId);
        if (obj == null) return null;
        return obj.getType();
    }

    public VideoGroupVO getVideoGroupInfo(Long videoGroupId) {
        var self = SpringContextUtil.getBean(getClass());
        var type = self.getVideoGroupType(videoGroupId);

        if (Objects.equals(type, VideoGroup.Type.PLAIN_VIDEO_GROUP)) {
            return plainVideoGroupServiceBase.getVideoGroupInfo(videoGroupId);
        } else if (Objects.equals(type, VideoGroup.Type.ANIME_VIDEO_GROUP)) {
            return bangumiVideoGroupServiceBase.getVideoGroupInfo(videoGroupId);
        } else return null;
    }
}
