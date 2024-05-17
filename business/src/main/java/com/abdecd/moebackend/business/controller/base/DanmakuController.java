package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.danmaku.AddDanmakuDTO;
import com.abdecd.moebackend.business.pojo.vo.danmaku.DanmakuVO;
import com.abdecd.moebackend.business.service.danmaku.DanmakuService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "弹幕接口")
@RestController
@RequestMapping("/video/danmaku")
public class DanmakuController {
    @Autowired
    private DanmakuService danmakuService;

    @Operation(summary = "获取弹幕")
    @GetMapping("")
    public Result<List<DanmakuVO>> getDanmaku(
            @RequestParam Long videoId,
            @RequestParam(defaultValue = "1") Integer segmentIndex
    ) {
        return Result.success(danmakuService.getDanmaku(videoId, segmentIndex));
    }

    @Operation(summary = "添加弹幕")
    @PostMapping("add")
    public Result<Long> addDanmaku(@RequestBody @Valid AddDanmakuDTO addDanmakuDTO) {
        return Result.success(danmakuService.addDanmaku(addDanmakuDTO));
    }

    @Operation(summary = "撤回弹幕")
    @PostMapping("delete")
    public Result<String> deleteDanmaku(@RequestBody Map<String, Long> map) {
        danmakuService.deleteDanmaku(map.get("id"));
        return Result.success();
    }
}
