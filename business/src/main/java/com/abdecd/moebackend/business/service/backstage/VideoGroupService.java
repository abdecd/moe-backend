package com.abdecd.moebackend.business.service.backstage;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupListVO;
import com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup.VideoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoVo;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface VideoGroupService {
    /**
     * 新增视频组，并返回新增id
     */
    Long insert(VideoGroup videoGroupDTO, MultipartFile cover);

    /**
     * 根据id删除视频组
     */
    void delete(Long id);

    /**
     * 更新视频组
     */
    void update(VideoGroupDTO videoGroupDTO);

    /**
     * 根据id获取视频组
     */
    VideoGroupVO getById(Long id);

    /**
     * 根据id获取视频组目录
     */
    ArrayList<VideoVo> getContentById(Long id);

    /**
     * 根据id获取视频组类型
     */
    Byte getTypeByVideoId(Long vid);

    /**
     * 根据id获取视频组分页信息
     */
    VideoGroupListVO getVideoGroupList(Integer page, Integer pageSize);

    /**
     * *
     * 更具id更新视频组信息
     */
    void update(@Valid BangumiVideoGroupUpdateDTO videoGroup);

    void deleteVideoGroup(Long id);
}
