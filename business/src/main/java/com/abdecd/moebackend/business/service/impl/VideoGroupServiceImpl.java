package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.*;
import com.abdecd.moebackend.business.dao.mapper.*;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.*;
import com.abdecd.moebackend.business.service.FileService;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.common.constant.VideoGroupConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
public class VideoGroupServiceImpl implements VIdeoGroupService {
    @Resource
    private VIdeoGroupMapper vIdeoGroupMapper;

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
            String randomImageName = UUID.randomUUID().toString() + ".jpg";
            coverPath =  fileService.uploadFile(videoGroupDTO.getCover(),randomImageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VideoGroup videoGroup = new VideoGroup();
        videoGroup.setUserId(uid)
                .setTitle(videoGroupDTO.getTitle())
                .setDescription(videoGroupDTO.getDescription())
                .setCover(coverPath)
                .setCreate_time(videoGroupDTO.getDate())
                .setWeight(VideoGroupConstant.DEFAULT_WEIGHT)
                .setType(VideoGroupConstant.COMMON_VIDEO_GROUP);

        vIdeoGroupMapper.insertVideoGroup(videoGroup);

        return  videoGroup.getId();
    }

    @Override

    public void delete(Long id) {
        VideoGroup videoGroup = new VideoGroup();
        videoGroup.setId(id);
        vIdeoGroupMapper.deleteById(videoGroup);
    }

    @Override
    public void update(VIdeoGroupDTO videoGroupDTO) {
        String coverPath = new String();

        if(videoGroupDTO.getCover() != null)
        {
            try {
                //TODO 文件没有存下来
                String randomImageName = UUID.randomUUID().toString() + ".jpg";
                coverPath =  fileService.uploadFile(videoGroupDTO.getCover(),randomImageName);
            } catch (IOException e) {
                throw new BaseException("文件存储失败");
            }
        }

        VideoGroup videoGroup = new VideoGroup();
        BeanUtils.copyProperties(videoGroupDTO,videoGroup);
        videoGroup.setCover(coverPath);

        vIdeoGroupMapper.update(videoGroup);
    }

    @Override
    public VideoGroupVO getById(Long id) {
        VideoGroupVO videoGroupVO = new VideoGroupVO();
        VideoGroup videoGroup = vIdeoGroupMapper.selectById(id);
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
    public Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String date = dtf.format(ldt);

        Long uid = UserContext.getUserId();

        String coverPath;

        try {
            //TODO 文件没有存下来
            String randomImageName = UUID.randomUUID().toString() + ".jpg";
            coverPath =  fileService.uploadFile(bangumiVideoGroupAddDTO.getCover(),randomImageName);
        } catch (IOException e) {
            throw new BaseException("文件存储失败");
        }

        VideoGroup videoGroup = new VideoGroup();

        videoGroup.setTitle(bangumiVideoGroupAddDTO.getTitle());
        videoGroup.setDescription(bangumiVideoGroupAddDTO.getDescription());
        videoGroup.setCover(coverPath);
        videoGroup.setCreate_time(date);
        videoGroup.setUserId(uid);
        videoGroup.setWeight(VideoGroupConstant.DEFAULT_WEIGHT);
        videoGroup.setType(VideoGroupConstant.COMMON_VIDEO_GROUP);


        vIdeoGroupMapper.insertVideoGroup(videoGroup);

        for(Integer tagid : bangumiVideoGroupAddDTO.getTagIds()){
            VideoGroupAndTag videoGroupAndTag = new VideoGroupAndTag();
            videoGroupAndTag.setVideo_group_id(videoGroup.getId());
            videoGroupAndTag.setTag_id(Long.valueOf(tagid));
            videoGroupandTagMapper.insert(videoGroupAndTag);
        }

        return videoGroup.getId();
    }

    @Override
    public void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO) {
        String coverPath = new String();

        if(bangumiVideoGroupUpdateDTO.getCover() != null)
        {
            try {
                //TODO 文件没有存下来
                String randomImageName = UUID.randomUUID().toString() + ".jpg";
                coverPath =  fileService.uploadFile(bangumiVideoGroupUpdateDTO.getCover(),randomImageName);
            } catch (IOException e) {
                throw new BaseException("文件存储失败");
            }
        }

        VideoGroup videoGroup = new VideoGroup();
        BeanUtils.copyProperties(bangumiVideoGroupUpdateDTO,videoGroup);
        videoGroup.setCover(coverPath);

        vIdeoGroupMapper.update(videoGroup);
    }

    @Override
    public BangumiVideoGroupVO getByVideoId(Long videoGroupId) {
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        VideoGroup videoGroup = vIdeoGroupMapper.selectById(videoGroupId);
        if(videoGroup == null)
        {
            throw new BaseException("视频组缺失");
        }
        bangumiVideoGroupVO.setVideoGroupId(videoGroupId);
        bangumiVideoGroupVO.setCover(videoGroup.getCover());
        bangumiVideoGroupVO.setDescription(videoGroup.getDescription());
        bangumiVideoGroupVO.setTitle(videoGroup.getTitle());
        bangumiVideoGroupVO.setType(videoGroup.getType());

        ArrayList<Long> tagIds = videoGroupandTagMapper.selectByVid(videoGroupId);
        ArrayList<VideoGroupTag> videoGroupTagList = new ArrayList<>();

        for (Long id_ : tagIds) {
            VideoGroupTag tag = videoGroupTagMapper.selectById(id_);
            if(tag != null)
                videoGroupTagList.add(tag);
        }

        bangumiVideoGroupVO.setTags(videoGroupTagList);

        UploaderVO uploaderVO = new UploaderVO();
        uploaderVO.setId(videoGroup.getUserId());
        PlainUserDetail plainUserDetail =  plainUserDetailMapper.selectByUid(videoGroup.getUserId());
        if(plainUserDetail != null){
            uploaderVO.setAvatar(plainUserDetail.getAvatar());
            uploaderVO.setNickname(plainUserDetail.getNickname());
        }

        bangumiVideoGroupVO.setUploader(uploaderVO);

        return  bangumiVideoGroupVO;
    }

    @Override
    public Integer getTypeByVideoId(Long aLong) {
        VideoGroup videoGroup = vIdeoGroupMapper.selectById(aLong);
        return videoGroup.getType();
    }

    @Override
    public VideoGroupListVO getVideoGroupList(Integer page, Integer pageSize) {
        VideoGroupListVO videoGroupListVO = new VideoGroupListVO();
        videoGroupListVO.setRecords(new ArrayList<>());

        Integer offset = (page - 1) * pageSize;
        ArrayList<VideoGroup> videoGroups = vIdeoGroupMapper.selectbyPage(offset,pageSize);

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
