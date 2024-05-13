package com.abdecd.moebackend.business.service.videogroup;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.common.util.SpringContextUtil;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupAndTagMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupTagMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoMapper;
import com.abdecd.moebackend.business.pojo.dto.videogroup.PlainVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.videogroup.PlainVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.ContentsItemVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.PlainVideoGroupVO;
import com.abdecd.moebackend.business.service.FileService;
import com.abdecd.moebackend.business.service.PlainUserService;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoService videoService;
    @Autowired
    private FileService fileService;

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
        List<VideoGroupTag> tags = new ArrayList<>();
        if (tagIds != null && !tagIds.isEmpty()) tags = videoGroupTagMapper.selectBatchIds(tagIds.stream().map(VideoGroupAndTag::getTagId).toList());

        var vo = new PlainVideoGroupVO();
        BeanUtils.copyProperties(base, vo);
        vo.setUploader(uploaderVO);
        vo.setTags(tags);
        return vo;
    }

    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_CONTENTS_CACHE, key = "#videoGroupId", unless = "#result == null")
    public List<ContentsItemVO> getContents(Long videoGroupId) {
        var videoList = videoMapper.selectList(new LambdaQueryWrapper<Video>()
                .eq(Video::getVideoGroupId, videoGroupId)
        );
        return new ArrayList<>(
                videoList.stream().map(video -> new ContentsItemVO()
                        .setVideoId(video.getId())
                        .setVideoGroupId(video.getVideoGroupId())
                        .setIndex(video.getIndex())
                        .setTitle(video.getTitle())
                        .setVideoCover(video.getCover())
                ).toList()
        );
    }

    @Transactional
    public Long addVideoGroup(PlainVideoGroupAddDTO plainVideoGroupAddDTO) {
        var entity = plainVideoGroupAddDTO.toEntity(UserContext.getUserId());
        videoGroupMapper.insert(entity);

        // 封面处理
        var coverUrl = plainVideoGroupAddDTO.getCover();
        try {
            var cover = fileService.changeTmpFileToStatic(
                    coverUrl,
                    "/video-group/" + entity.getId(),
                    "cover" + coverUrl.substring(coverUrl.lastIndexOf('.'))
            );
            if (cover.isEmpty()) throw new Exception(); // will be caught
            videoGroupMapper.updateById(entity.setCover(cover));
        } catch (Exception e) {
            throw new BaseException(MessageConstant.INVALID_FILE_PATH);
        }

        var tags = Arrays.stream(plainVideoGroupAddDTO.getTagIds())
                .map(tagId -> new VideoGroupAndTag().setTagId(tagId).setVideoGroupId(entity.getId()))
                .toList();
        Db.saveBatch(tags);
        return entity.getId();
    }

    @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CACHE, key = "#plainVideoGroupUpdateDTO.id")
    @Transactional
    public void updateVideoGroup(PlainVideoGroupUpdateDTO plainVideoGroupUpdateDTO) {
        checkUserHaveTheGroup(plainVideoGroupUpdateDTO.getId());

        var entity = plainVideoGroupUpdateDTO.toEntity(UserContext.getUserId());
        videoGroupMapper.updateById(entity);

        // 封面处理
        var coverUrl = plainVideoGroupUpdateDTO.getCover();
        if (coverUrl != null) {
            try {
                var cover = fileService.changeTmpFileToStatic(
                        coverUrl,
                        "/video-group/" + entity.getId(),
                        "cover" + coverUrl.substring(coverUrl.lastIndexOf('.'))
                );
                if (cover.isEmpty()) throw new Exception(); // will be caught
                videoGroupMapper.updateById(entity.setCover(cover));
            } catch (Exception e) {
                throw new BaseException(MessageConstant.INVALID_FILE_PATH);
            }
        }

        if (plainVideoGroupUpdateDTO.getTagIds() != null) {
            var tags = Arrays.stream(plainVideoGroupUpdateDTO.getTagIds())
                    .map(tagId -> new VideoGroupAndTag().setTagId(tagId).setVideoGroupId(entity.getId()))
                    .toList();
            videoGroupAndTagMapper.delete(new LambdaQueryWrapper<VideoGroupAndTag>().eq(VideoGroupAndTag::getVideoGroupId, entity.getId()));
            Db.saveBatch(tags);
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CACHE, key = "#videoGroupId"),
            @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CONTENTS_CACHE, key = "#videoGroupId")
    })
    @Transactional
    public void deleteVideoGroup(Long videoGroupId) {
        checkUserHaveTheGroup(videoGroupId);
        videoGroupMapper.deleteById(videoGroupId);
        videoGroupAndTagMapper.delete(new LambdaQueryWrapper<VideoGroupAndTag>().eq(VideoGroupAndTag::getVideoGroupId, videoGroupId));
        // 删视频
        for (var video : videoMapper.selectList(new LambdaQueryWrapper<Video>().eq(Video::getVideoGroupId, videoGroupId)))
            videoService.deleteVideo(video.getId());
        // 删文件夹
        fileService.deleteDirInSystem("/video-group/" + videoGroupId);
    }

    /**
     * 检验空值以及是否是当前用户的视频组
     * @param videoGroupId :
     */
    public void checkUserHaveTheGroup(Long videoGroupId) {
        var videoGroupService = SpringContextUtil.getBean(VideoGroupServiceBase.class);
        videoGroupService.checkUserHaveTheGroup(videoGroupId);
    }
}
