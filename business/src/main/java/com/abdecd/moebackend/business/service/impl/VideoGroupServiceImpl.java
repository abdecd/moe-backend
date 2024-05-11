package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.*;
import com.abdecd.moebackend.business.dao.mapper.*;
import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.*;
import com.abdecd.moebackend.business.service.FileService;
import com.abdecd.moebackend.business.service.VideoGroupService;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.moebackend.common.constant.VideoGroupConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
public class VideoGroupServiceImpl implements VideoGroupService {
    @Resource
    private VideoGroupMapper videoGroupMapper;

    @Resource
    private FileService fileService;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoGroupTagMapper videoGroupTagMapper;

    @Resource
    private PlainUserDetailMapper plainUserDetailMapper;

    @Resource
    private VideoGroupAndTagMapper videoGroupandTagMapper;


    @Override
    public Long insert(VIdeoGroupDTO videoGroupDTO) {
        Long uid = UserContext.getUserId();

        String coverPath;

        try {
            //TODO 文件没有存下来
            String randomImageName = UUID.randomUUID() + ".jpg";
            coverPath =  fileService.uploadFile(videoGroupDTO.getCover(),randomImageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VideoGroup videoGroup = new VideoGroup();
        if (videoGroupDTO.getDate() != null) {
            videoGroup.setUserId(uid)
                    .setTitle(videoGroupDTO.getTitle())
                    .setDescription(videoGroupDTO.getDescription())
                    .setCover(coverPath)
                    .setCreateTime(LocalTime.parse(videoGroupDTO.getDate()))
                    .setWeight(VideoGroupConstant.DEFAULT_WEIGHT)
                    .setType(VideoGroupConstant.COMMON_VIDEO_GROUP);
        }

        videoGroupMapper.insertVideoGroup(videoGroup);

        return  videoGroup.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CACHE,key = "#id")
    public void delete(Long id) {
        VideoGroup videoGroup = new VideoGroup();
        videoGroup.setId(id);
        videoGroupMapper.deleteById(videoGroup);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CACHE,key = "#videoGroupDTO.id")
    public void update(VIdeoGroupDTO videoGroupDTO) {
        String coverPath = "";

        if(videoGroupDTO.getCover() != null)
        {
            try {
                //TODO 文件没有存下来
                String randomImageName = UUID.randomUUID() + ".jpg";
                coverPath =  fileService.uploadFile(videoGroupDTO.getCover(),randomImageName);
            } catch (IOException e) {
                throw new BaseException("文件存储失败");
            }
        }

        VideoGroup videoGroup = new VideoGroup();
        BeanUtils.copyProperties(videoGroupDTO,videoGroup);
        videoGroup.setCover(coverPath);

        videoGroupMapper.update(videoGroup);
    }

    @Override
    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_CACHE,key = "#id")
    public VideoGroupVO getById(Long id) {
        VideoGroupVO videoGroupVO = new VideoGroupVO();
        VideoGroup videoGroup = videoGroupMapper.selectById(id);
        if(videoGroup == null)
        {
            throw new BaseException("视频组缺失");
        }
        videoGroupVO.setVideoGroupId(id);
        videoGroupVO.setCover(videoGroup.getCover());
        videoGroupVO.setDescription(videoGroup.getDescription());
        videoGroupVO.setTitle(videoGroup.getTitle());
        videoGroupVO.setType(videoGroup.getType());

        ArrayList<Long> tagIds = videoGroupandTagMapper.selectByVid(id);
        ArrayList<VideoGroupTag> videoGroupTagList = new ArrayList<>();

        for (Long id_ : tagIds) {
            VideoGroupTag tag = videoGroupTagMapper.selectById(id_);
            if(tag != null)
                videoGroupTagList.add(tag);
        }

        videoGroupVO.setTags(videoGroupTagList);

        UploaderVO uploaderVO = new UploaderVO();
        uploaderVO.setId(videoGroup.getUserId());
        PlainUserDetail plainUserDetail =  plainUserDetailMapper.selectByUid(videoGroup.getUserId());
        if(plainUserDetail != null){
            uploaderVO.setAvatar(plainUserDetail.getAvatar());
            uploaderVO.setNickname(plainUserDetail.getNickname());
        }

        videoGroupVO.setUploader(uploaderVO);

        return  videoGroupVO;
    }

    @Override
    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_CONTENT_CACHE,key = "#id")
    public ArrayList<VideoVo> getContentById(Long id) {
        ArrayList<VideoVo> videoVOList = new ArrayList<>();
        ArrayList<Video> videoList = videoMapper.getByGroupid(id);

        for(Video video : videoList){
            VideoVo videoVo = new VideoVo();

            videoVo.setVideoId(String.valueOf(video.getId()));
            videoVo.setVideoCover(video.getCover());
            videoVo.setIndex(String.valueOf(video.getIndex()));
            videoVo.setTitle(video.getTitle());
            videoVo.setVideoGroupId(String.valueOf(id));

            videoVOList.add(videoVo);
        }

        return videoVOList;
    }

    @Override
    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_TYPE_CACHE,key = "#id")
    public Integer getTypeByVideoId(Long id) {
        VideoGroup videoGroup = videoGroupMapper.selectById(id);
        return videoGroup.getType();
    }

    @Override
    @Cacheable(cacheNames = RedisConstant.VIDEO_GROUP_PAGE_CACHE,key = "#page + ':' + #pageSize")
    public VideoGroupListVO getVideoGroupList(Integer page, Integer pageSize) {
        VideoGroupListVO videoGroupListVO = new VideoGroupListVO();
        videoGroupListVO.setRecords(new ArrayList<>());

        Integer offset = (page - 1) * pageSize;
        ArrayList<VideoGroup> videoGroups = videoGroupMapper.selectbyPage(offset,pageSize);

        for(VideoGroup videoGroup : videoGroups){
            VideoGroupVO videoGroupVO = new VideoGroupVO();

            videoGroupVO.setVideoGroupId(videoGroup.getId());
            videoGroupVO.setCover(videoGroup.getCover());
            videoGroupVO.setDescription(videoGroup.getDescription());
            videoGroupVO.setTitle(videoGroup.getTitle());
            videoGroupVO.setType(videoGroup.getType());

            ArrayList<Long> tagIds = videoGroupandTagMapper.selectByVid(videoGroup.getId());
            ArrayList<VideoGroupTag> videoGroupTagList = new ArrayList<>();

            for (Long id_ : tagIds) {
                VideoGroupTag tag = videoGroupTagMapper.selectById(id_);
                if(tag != null)
                    videoGroupTagList.add(tag);
            }

            videoGroupVO.setTags(videoGroupTagList);

            UploaderVO uploaderVO = new UploaderVO();
            uploaderVO.setId(videoGroup.getUserId());
            PlainUserDetail plainUserDetail =  plainUserDetailMapper.selectByUid(videoGroup.getUserId());
            if(plainUserDetail != null){
                uploaderVO.setAvatar(plainUserDetail.getAvatar());
                uploaderVO.setNickname(plainUserDetail.getNickname());
            }

            videoGroupVO.setUploader(uploaderVO);

            videoGroupListVO.getRecords().add(videoGroupVO);
        }

        videoGroupListVO.setTotal(videoGroupListVO.getRecords().size());
        return videoGroupListVO;
    }
}
