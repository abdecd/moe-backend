package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.pojo.dto.backstage.videogroup.VideoGroupStatusDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupListVO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoForceVO;
import com.abdecd.moebackend.business.service.backstage.VideoGroupService;
import com.abdecd.moebackend.business.service.video.VideoService;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Tag(name = "后台通用视频组接口")
@Slf4j
@RestController
@RequestMapping("/backstage/video-group")
public class VideoGroupController {
    @Resource
    private VideoGroupService videoGroupService;

    @Resource
    private VideoService videoService;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "视频组类型获取", description = "data字段返回视频组类型")
    @GetMapping("/type")
    public Result<Integer> getVideoGroupType(@Valid @RequestParam("videoGroupId") String videoGroupId) {
        Integer videoGroupType = Integer.valueOf(videoGroupService.getTypeByVideoId(Long.valueOf(videoGroupId)));
        return Result.success(videoGroupType);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "视频组列表获取", description = "data字段返回视频组列表")
    @GetMapping("/list")
    public Result<VideoGroupListVO> getVideoGroupList(@Valid @RequestParam("page") Integer page, @Valid @RequestParam("pageSize") Integer pageSize) {
        VideoGroupListVO videoGroupListVO = videoGroupService.getVideoGroupList(page, pageSize);
        return Result.success(videoGroupListVO);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "视频组对应视频获取", description = "data字段返回视频组对应视频")
    @GetMapping("/list-all-video")
    public Result<ArrayList<VideoForceVO>> getAllVideo(@Valid @RequestParam("videoGroupId") Long videoGroupId) {
        ArrayList<VideoForceVO> videoCompleteVOArrayList = new ArrayList<>();
        ArrayList<Video> videoList = videoService.getVideoListByGid(videoGroupId);

        for (Video video : videoList) {
            VideoForceVO videoVO = videoService.getVideoForce(video.getId());

            videoCompleteVOArrayList.add(videoVO);
        }
        return Result.success(videoCompleteVOArrayList);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "视频组状态更改", description = "data字段返回是否成功")
    @PostMapping("/status")
    public Result<String> changeVideoGroupStatus(@RequestBody @Valid VideoGroupStatusDTO videoGroupStatusDTO) {
        var affected = videoGroupService.changeStatus(videoGroupStatusDTO.getId(), videoGroupStatusDTO.getStatus());
        if (affected == 0) {
            return Result.error(404, "视频组不存在或状态未改变");
        }
        return Result.success();
    }
}
