package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoFullDTO;
import com.abdecd.moebackend.business.service.video.VideoService;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backstage/video")
public class VideoBackController {

    @Resource
    private VideoService videoService;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "添加视频")
    @PostMapping("/add")
    public Result<Long> add(@Valid AddVideoFullDTO addVideoDTO){
        Long id = videoService.addVideo(addVideoDTO, addVideoDTO.getBecomeVideoStatus());
        return Result.success(id);
    }
}
