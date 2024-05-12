package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup.VideoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoVo;
import com.abdecd.moebackend.business.service.VideoGroupAndTagService;
import com.abdecd.moebackend.business.service.VideoGroupService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Tag(name = "普通视频组接口")
@Slf4j
@RestController
@RequestMapping("/backstage/plain-video-group")
public class PlainVideoGroupController {

    @Resource
    private VideoGroupService videoGroupService;

    @Resource
    private VideoGroupAndTagService videoGroupAndTagService;

    @Operation(summary = "普通视频组添加", description = "data字段返回新增普通视频组id")
    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    public Result<Long> addVideoGroup(@Valid VideoGroupDTO videoGroupDTO) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String date = dtf.format(ldt);
        videoGroupDTO.setDate(date);

        Long groupId = videoGroupService.insert(videoGroupDTO);

        if (videoGroupDTO.getTagIds() != null) {
            for (String i : videoGroupDTO.getTagIds()) {
                Long tagId = Long.valueOf(i);
                videoGroupAndTagService.insert(tagId, groupId);
            }
        }

        return Result.success(groupId);
    }

    @Operation(summary = "普通视频组删除")
    @PostMapping(value = "/delete")
    public Result<String> delVideoGroup(@Valid @RequestParam("id") Long id) {
        videoGroupService.delete(id);
        return Result.success();
    }

    @Operation(summary = "普通视频组更新")
    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    @CacheEvict(cacheNames = "videoGroup", key = "#videoGroupDTO.id")
    public Result<String> updateVideoGroup(@Valid VideoGroupDTO videoGroupDTO) {
        videoGroupService.update(videoGroupDTO);
        return Result.success();
    }

    @Operation(summary = "普通视频组获取", description = "data字段返回普通视频组信息")
    @GetMapping("")
    public Result<VideoGroupVO> getVideoGroup(@Valid @RequestParam("id") Long id) {
        VideoGroupVO videoGroupVO = videoGroupService.getById(id);
        return Result.success(videoGroupVO);
    }

    @Operation(summary = "普通视频组目录获取", description = "data字段返回普通视频组目录")
    @GetMapping("/contents")
    public Result<ArrayList<VideoVo>> getVideoGroupContent(@Valid @RequestParam("id") Long id) {
        ArrayList<VideoVo> videoVoList = videoGroupService.getContentById(id);
        return Result.success(videoVoList);
    }
}
