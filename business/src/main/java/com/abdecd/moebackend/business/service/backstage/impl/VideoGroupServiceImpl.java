package com.abdecd.moebackend.business.service.backstage.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.abdecd.moebackend.business.dao.mapper.*;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup.VideoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupListVO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoVo;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.pojo.vo.statistic.StatisticDataVO;
import com.abdecd.moebackend.business.service.fileservice.FileService;
import com.abdecd.moebackend.business.service.backstage.VideoGroupService;
import com.abdecd.moebackend.business.service.statistic.StatisticService;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.moebackend.common.constant.VideoGroupConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    private StatisticService statisticService;

    @Resource
    private VideoGroupTagMapper videoGroupTagMapper;

    @Resource
    private PlainUserDetailMapper plainUserDetailMapper;

    @Resource
    private VideoGroupAndTagMapper videoGroupandTagMapper;


    @Override
    public Long insert(VideoGroup videoGroup, MultipartFile cover) {
        Long uid = UserContext.getUserId();

        String coverPath = "";

        videoGroup.setCover(coverPath)
                .setUserId(uid)
                .setWeight(VideoGroupConstant.DEFAULT_WEIGHT)
                .setType(VideoGroupConstant.COMMON_VIDEO_GROUP);

        videoGroupMapper.insertVideoGroup(videoGroup);

        try {
            //TODO 文件没有存下来
            String coverPath_ = "/video-group/" + videoGroup.getId()+ "/" + cover.getName() + ".jpg";
            coverPath =   fileService.uploadFile(cover,coverPath_);
        } catch (IOException e) {
            throw new BaseException("文件存储失败");
        }
        videoGroup.setCover(coverPath);

        videoGroupMapper.update(videoGroup);

        return  videoGroup.getId();
    }

    @Override
    public void delete(Long id) {
        VideoGroup videoGroup = new VideoGroup();
        videoGroup.setId(id);
        videoGroupMapper.deleteById(videoGroup);
    }

    @Override
    public void update(VideoGroupDTO videoGroupDTO) {
        String coverPath = "";

        if(videoGroupDTO.getCover() != null)
        {
            try {
                //TODO 文件没有存下来
                String coverPath_ = "/video-group/" + videoGroupDTO.getId()+ "/" + videoGroupDTO.getCover().getName() + ".jpg";
                coverPath =   fileService.uploadFile(videoGroupDTO.getCover(),coverPath_);
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
    public VideoGroupVO getById(Long id) {
        VideoGroupVO videoGroupVO = new VideoGroupVO();
        VideoGroup videoGroup = videoGroupMapper.selectById(id);
        if(videoGroup == null) {
            throw new BaseException("视频组缺失");
        }

        videoGroupVO.setId(id);
        videoGroupVO.setCover(videoGroup.getCover());
        videoGroupVO.setDescription(videoGroup.getDescription());
        videoGroupVO.setTitle(videoGroup.getTitle());
        videoGroupVO.setType(videoGroup.getType());

        videoGroupVO.setTags(videoGroupVO.getTags());

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
    public Byte getTypeByVideoId(Long id) {
        VideoGroup videoGroup = videoGroupMapper.selectById(id);
        return videoGroup.getType();
    }

    @Override
    public VideoGroupListVO getVideoGroupList(Integer page, Integer pageSize) {
        VideoGroupListVO videoGroupListVO = new VideoGroupListVO();
        videoGroupListVO.setRecords(new ArrayList<>());

        Integer offset = (page - 1) * pageSize;
        ArrayList<VideoGroup> videoGroups = videoGroupMapper.selectbyPage(offset,pageSize);

        for(VideoGroup videoGroup : videoGroups){
            VideoGroupVO videoGroupVO = new VideoGroupVO();

            videoGroupVO.setId(videoGroup.getId());
            videoGroupVO.setCover(videoGroup.getCover());
            videoGroupVO.setDescription(videoGroup.getDescription());
            videoGroupVO.setTitle(videoGroup.getTitle());
            videoGroupVO.setType(videoGroup.getType());
            videoGroupVO.setCreateTime(String.valueOf(videoGroup.getCreateTime()));

            videoGroupVO.setTags(videoGroupVO.getTags());

            UploaderVO uploaderVO = new UploaderVO();
            uploaderVO.setId(videoGroup.getUserId());
            PlainUserDetail plainUserDetail =  plainUserDetailMapper.selectByUid(videoGroup.getUserId());
            if(plainUserDetail != null){
                uploaderVO.setAvatar(plainUserDetail.getAvatar());
                uploaderVO.setNickname(plainUserDetail.getNickname());
            }

            videoGroupVO.setUploader(uploaderVO);

            StatisticDataVO statisticDataVO = statisticService.getStatisticData(videoGroupVO.getId());
            videoGroupVO.setWatchCnt(Math.toIntExact(statisticDataVO.getWatchCnt()));
            videoGroupVO.setFavoriteCnt(Math.toIntExact(statisticDataVO.getFavoriteCnt()));
            videoGroupVO.setLikeCnt(Math.toIntExact(statisticDataVO.getLikeCnt()));
            videoGroupVO.setUserLike(statisticDataVO.getUserLike());
            videoGroupVO.setUserFavorite(statisticDataVO.getUserFavorite());
            videoGroupVO.setCommentCnt(Math.toIntExact(statisticDataVO.getCommentCnt()));
            videoGroupVO.setDanmakuCnt(Math.toIntExact(statisticDataVO.getDanmakuCnt()));

            videoGroupListVO.getRecords().add(videoGroupVO);
        }

        videoGroupListVO.setTotal(videoGroupListVO.getRecords().size());
        return videoGroupListVO;
    }

    @Override
    public void update(@Valid BangumiVideoGroupUpdateDTO videoGroup) {
        String coverPath = null;

        if(videoGroup.getCover() != null)
        {
            try {
                //TODO 文件没有存下来
                String coverName = videoGroup.getCover().getName() + ".jpg";
                coverPath =  fileService.uploadFile(videoGroup.getCover(),coverName);
            } catch (IOException e) {
                throw new BaseException("文件存储失败");
            }
        }

        videoGroupMapper.update(
                new VideoGroup()
                        .setId(videoGroup.getId())
                        .setVideoGroupStatus(Byte.valueOf(videoGroup.getStatus()))
                        .setTitle(videoGroup.getTitle())
                        .setCover(coverPath)
                        .setDescription(videoGroup.getDescription())
                        .setTags(videoGroup.getTags())
        );
    }
}
