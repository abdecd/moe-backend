package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
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

    @Operation(summary = "添加的视频是否正在处理")
    @GetMapping("check-add-pending")
    public Result<Boolean> checkAddVideoPending(@RequestParam Long videoId) {
        return Result.success(videoService.checkAddVideoPending(videoId));
    }
}
