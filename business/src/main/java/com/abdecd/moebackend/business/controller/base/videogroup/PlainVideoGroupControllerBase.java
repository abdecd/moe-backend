package com.abdecd.moebackend.business.controller.base.videogroup;

import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupVO;
import com.abdecd.moebackend.business.service.videogroup.PlainVideoGroupServiceBase;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "普通视频组接口")
@RestController
@RequestMapping("plain-video-group")
public class PlainVideoGroupControllerBase {
    @Autowired
    private PlainVideoGroupServiceBase plainVideoGroupServiceBase;

    @Operation(summary = "获取视频组信息")
    @GetMapping("")
    public Result<VideoGroupVO> getVideoGroup(@RequestParam Long videoGroupId) {
        return Result.success(plainVideoGroupServiceBase.getVideoGroupInfo(videoGroupId));
    }
}
