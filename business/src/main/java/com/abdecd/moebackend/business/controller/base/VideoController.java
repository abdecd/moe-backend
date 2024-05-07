package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.DeleteVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @Operation(summary = "添加视频")
    @PostMapping("add")
    public Result<Long> addVideo(@RequestBody @Valid AddVideoDTO addVideoDTO) {
        return Result.success(videoService.addVideo(addVideoDTO));
    }

    @Operation(summary = "添加或修改的视频是否正在处理")
    @GetMapping("check-video-pending")
    public Result<Boolean> checkAddVideoPending(@RequestParam Long videoId) {
        return Result.success(videoService.checkVideoPending(videoId));
    }

    @Operation(summary = "修改视频")
    @PostMapping("update")
    public Result<String> updateVideo(@RequestBody @Valid UpdateVideoDTO updateVideoDTO) {
        videoService.updateVideo(updateVideoDTO);
        return Result.success();
    }

    @Operation(summary = "删除视频")
    @PostMapping("delete")
    public Result<String> deleteVideo(@RequestBody @Valid DeleteVideoDTO deleteVideoDTO) {
        videoService.deleteVideo(deleteVideoDTO.getId());
        return Result.success();
    }

    @Operation(summary = "获取视频")
    @GetMapping("")
    public Result<VideoVO> getVideo(@RequestParam Long id) {
        return Result.success(videoService.getVideo(id));
    }
}
