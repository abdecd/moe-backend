package com.abdecd.moebackend.business.service.impl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.VIdeoGroupMapper;
import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupVO;
import com.abdecd.moebackend.business.service.FileService;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.common.constant.VideoGroupConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class VideoGroupServiceImpl implements VIdeoGroupService {
    @Resource
    private VIdeoGroupMapper vIdeoGroupMapper;

    @Resource
    private FileService fileService;

    @Override
    public Long insert(VIdeoGroupDTO videoGroupDTO) {
        //TODO 从token解析userid
        Long uid = 1L;

        String coverPath;

        try {
            //TODO 文件没有存下来
            String randomImageName = UUID.randomUUID().toString() + ".jpg";
            coverPath =  fileService.uploadFile(videoGroupDTO.getCover(),randomImageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VideoGroup videoGroup = new VideoGroup();
        videoGroup.setUser_id(uid)
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
                throw new RuntimeException(e);
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
        if(videoGroup != null)
        {
            //TODO 错误抛出，没有对应的数据
        }
        videoGroupVO.setVideoGroupId(id);
        videoGroupVO.setCover(videoGroup.getCover());
        videoGroupVO.setDescription(videoGroupVO.getDescription());
        videoGroupVO.setTitle(videoGroup.getTitle());

        // TODO Tags 和 User 的部分
        return  videoGroupVO;
    }
}
