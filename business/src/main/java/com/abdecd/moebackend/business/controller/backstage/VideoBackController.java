package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.BangumiTimeTable;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.BangumiTimeTableMapper;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoFullDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoFullDTO;
import com.abdecd.moebackend.business.service.video.VideoService;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.StatusConstant;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/backstage/video")
public class VideoBackController {
    @Resource
    private VideoService videoService;
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;
    @Autowired
    private BangumiTimeTableMapper bangumiTimeTableMapper;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "添加视频")
    @PostMapping("/add")
    public Result<Long> add(@RequestBody @Valid AddVideoFullDTO addVideoDTO){
        Long id = videoService.addVideo(addVideoDTO, addVideoDTO.getVideoStatusWillBe());
        if (
                Objects.equals(addVideoDTO.getVideoStatusWillBe(), Video.Status.PRELOAD)
                && addVideoDTO.getVideoPublishTime() != null
                && Objects.equals(videoGroupServiceBase.getVideoGroupType(addVideoDTO.getVideoGroupId()), VideoGroup.Type.ANIME_VIDEO_GROUP)
        ) {
            bangumiTimeTableMapper.insert(new BangumiTimeTable()
                    .setVideoId(id)
                    .setVideoGroupId(addVideoDTO.getVideoGroupId())
                    .setUpdateTime(addVideoDTO.getVideoPublishTime())
                    .setStatus(StatusConstant.ENABLE)
            );
        }
        return Result.success(id);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "修改视频")
    @PostMapping("update")
    public Result<String> update(@RequestBody @Valid UpdateVideoFullDTO dto) {
        videoService.updateVideo(dto, dto.getVideoStatusWillBe());
        if (
                Objects.equals(dto.getVideoStatusWillBe(), Video.Status.PRELOAD)
                && dto.getVideoPublishTime() != null
                && Objects.equals(videoGroupServiceBase.getVideoGroupType(dto.getVideoGroupId()), VideoGroup.Type.ANIME_VIDEO_GROUP)
        ) {
            if (bangumiTimeTableMapper.update(new LambdaUpdateWrapper<BangumiTimeTable>()
                    .eq(BangumiTimeTable::getVideoId, dto.getId())
                    .set(BangumiTimeTable::getVideoGroupId, dto.getVideoGroupId())
                    .set(BangumiTimeTable::getUpdateTime, dto.getVideoPublishTime())
                    .set(BangumiTimeTable::getStatus, StatusConstant.ENABLE)
            ) == 0) {
                bangumiTimeTableMapper.insert(new BangumiTimeTable()
                        .setVideoId(dto.getId())
                        .setVideoGroupId(dto.getVideoGroupId())
                        .setUpdateTime(dto.getVideoPublishTime())
                        .setStatus(StatusConstant.ENABLE)
                );
            }
        }
        return Result.success();
    }
}
