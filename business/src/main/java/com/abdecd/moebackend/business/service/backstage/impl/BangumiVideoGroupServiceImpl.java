package com.abdecd.moebackend.business.service.backstage.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.*;
import com.abdecd.moebackend.business.dao.mapper.*;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.bangumiVideoGroup.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.service.backstage.BangumiVideoGroupService;
import com.abdecd.moebackend.business.service.fileservice.FileService;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.moebackend.common.constant.VideoGroupConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
public class BangumiVideoGroupServiceImpl implements BangumiVideoGroupService {
    @Resource
    private BangumiVideoGroupMapper bangumiVideoGroupMapper;

    @Resource
    private VideoGroupMapper videoGroupMapper;

    @Resource
    private FileService fileService;

    @Resource
    private VideoGroupAndTagMapper videoGroupAndTagMapper;

    @Resource
    private PlainUserHistoryMapper plainUserHistoryMapper;

    @Resource
    private VideoGroupTagMapper videoGroupTagMapper;

    @Resource
    private PlainUserDetailMapper plainUserDetailMapper;

    @Override
    public void deleteByVid(Long id) {
        bangumiVideoGroupMapper.deleteByVid(id);
    }

    @Override
    public void insert(BangumiVideoGroup bangumiVideoGroup) {
        bangumiVideoGroupMapper.insert(bangumiVideoGroup);
    }

    @Override
    public void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO) {
        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();

        bangumiVideoGroup.setVideoGroupId(bangumiVideoGroupUpdateDTO.getId());
        Integer status = bangumiVideoGroupUpdateDTO.getStatus().equals("已完结")?1:0;
        if (bangumiVideoGroupUpdateDTO.getStatus() != null)
            bangumiVideoGroup.setStatus(status);
        if (bangumiVideoGroupUpdateDTO.getReleaseTime() != null) {
            bangumiVideoGroup.setReleaseTime(LocalDateTime.parse(bangumiVideoGroupUpdateDTO.getReleaseTime()));
        }
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupUpdateDTO.getUpdateAtAnnouncement());

        bangumiVideoGroupMapper.update(bangumiVideoGroup);
    }

    @Override
    public BangumiVideoGroupVO getByVid(Long vid) {
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        BangumiVideoGroup bangumiVideoGroup = bangumiVideoGroupMapper.selectByVid(vid);
        log.info("bangumiVideoGroup:{}", bangumiVideoGroup);

        bangumiVideoGroupVO.setReleaseTime(String.valueOf(bangumiVideoGroup.getReleaseTime()));
        bangumiVideoGroupVO.setUpdateAtAnnouncement(bangumiVideoGroup.getUpdateAtAnnouncement());
        bangumiVideoGroupVO.setStatus(bangumiVideoGroup.getStatus());

        return bangumiVideoGroupVO;
    }

    @Override
    public Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO) {
        LocalDateTime ldt = LocalDateTime.now();

        Long uid = UserContext.getUserId();

        String coverPath;

        try {
            //TODO 文件没有存下来
            String randomImageName = bangumiVideoGroupAddDTO.getCover().getName() + ".jpg";
            coverPath = fileService.uploadFile(bangumiVideoGroupAddDTO.getCover(), randomImageName);
        } catch (IOException e) {
            throw new BaseException("文件存储失败");
        }
        Byte status = (byte) (bangumiVideoGroupAddDTO.getStatus().equals("已完结") ? 1 : 0);

        VideoGroup videoGroup = new VideoGroup()
                .setTitle(bangumiVideoGroupAddDTO.getTitle())
                .setDescription(bangumiVideoGroupAddDTO.getDescription())
                .setCover(coverPath)
                .setCreateTime(ldt)
                .setUserId(uid)
                .setWeight(VideoGroupConstant.DEFAULT_WEIGHT)
                .setType(VideoGroupConstant.COMMON_VIDEO_GROUP)
                .setVideoGroupStatus(status)
                .setTags(bangumiVideoGroupAddDTO.getTags());


        videoGroupMapper.insertVideoGroup(videoGroup);

        String[] tags = bangumiVideoGroupAddDTO.getTags().split(";");
        for (String tagid : tags) {
            VideoGroupAndTag videoGroupAndTag = new VideoGroupAndTag();
            videoGroupAndTag.setVideoGroupId(videoGroup.getId());
            videoGroupAndTag.setTagId(Long.valueOf(tagid));
            videoGroupAndTagMapper.insert(videoGroupAndTag);
        }

        return videoGroup.getId();
    }

    @Override
    public BangumiVideoGroupVO getByVideoId(Long videoGroupId) {
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        VideoGroup videoGroup = videoGroupMapper.selectById(videoGroupId);

        if (videoGroup == null) {
            throw new BaseException("视频组缺失");
        }

        bangumiVideoGroupVO.setVideoGroupId(videoGroupId);
        bangumiVideoGroupVO.setCover(videoGroup.getCover());
        bangumiVideoGroupVO.setDescription(videoGroup.getDescription());
        bangumiVideoGroupVO.setTitle(videoGroup.getTitle());
        bangumiVideoGroupVO.setType(Integer.valueOf(videoGroup.getType()));
        bangumiVideoGroupVO.setCreateTime(String.valueOf(videoGroup.getCreateTime()));

        ArrayList<Long> tagIds = videoGroupAndTagMapper.selectByVid(videoGroupId);
        //ArrayList<VideoGroupTag> videoGroupTagList = new ArrayList<>();

       /* for (Long id_ : tagIds) {
            VideoGroupTag tag = videoGroupTagMapper.selectById(id_);
            if (tag != null)
                videoGroupTagList.add(tag);
        }*/

        bangumiVideoGroupVO.setTags(videoGroup.getTags());

        UploaderVO uploaderVO = new UploaderVO();
        uploaderVO.setId(videoGroup.getUserId());
        PlainUserDetail plainUserDetail = plainUserDetailMapper.selectByUid(videoGroup.getUserId());
        if (plainUserDetail != null) {
            uploaderVO.setAvatar(plainUserDetail.getAvatar());
            uploaderVO.setNickname(plainUserDetail.getNickname());
        }

        bangumiVideoGroupVO.setUploader(uploaderVO);

        return bangumiVideoGroupVO;
    }
}
