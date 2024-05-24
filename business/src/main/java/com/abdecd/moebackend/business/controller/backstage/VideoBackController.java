package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.BangumiTimeTable;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.mapper.BangumiTimeTableMapper;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoFullDTO;
import com.abdecd.moebackend.business.service.video.VideoService;
import com.abdecd.moebackend.common.constant.StatusConstant;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/backstage/video")
public class VideoBackController {
    @Resource
    private VideoService videoService;
    @Autowired
    private BangumiTimeTableMapper bangumiTimeTableMapper;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "添加视频")
    @PostMapping("/add")
    public Result<Long> add(@Valid AddVideoFullDTO addVideoDTO){
        Long id = videoService.addVideo(addVideoDTO, addVideoDTO.getVideoStatusWillBe());
        if (Objects.equals(addVideoDTO.getVideoStatusWillBe(), Video.Status.PRELOAD) && addVideoDTO.getVideoPublishTime() != null) {
            bangumiTimeTableMapper.insert(new BangumiTimeTable()
                    .setVideoId(id)
                    .setVideoGroupId(addVideoDTO.getVideoGroupId())
                    .setUpdateTime(addVideoDTO.getVideoPublishTime())
                    .setStatus(StatusConstant.ENABLE)
            );
        }
        return Result.success(id);
    }
}
