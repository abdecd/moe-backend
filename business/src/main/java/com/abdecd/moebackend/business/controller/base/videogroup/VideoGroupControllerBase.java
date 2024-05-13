package com.abdecd.moebackend.business.controller.base.videogroup;

import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupBigVO;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Tag(name = "视频组接口")
@RestController
@RequestMapping("/video-group")
public class VideoGroupControllerBase {
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;

    @Operation(summary = "获取视频组类型")
    @GetMapping("type")
    public Result<Byte> getVideoGroupType(@RequestParam Long id) {
        return Result.success(videoGroupServiceBase.getVideoGroupType(id));
    }

    @Async
    @Operation(summary = "获取视频组大接口")
    @GetMapping("")
    public CompletableFuture<Result<VideoGroupBigVO>> getVideoGroup(@RequestParam Long id) {
        return CompletableFuture.completedFuture(Result.success(videoGroupServiceBase.getBigVideoGroup(id)));
    }
}
