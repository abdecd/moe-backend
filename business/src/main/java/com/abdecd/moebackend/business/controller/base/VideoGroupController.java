package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.pojo.vo.common.commonVideoGroup.VideoCompleteVO;
import com.abdecd.moebackend.business.pojo.vo.common.commonVideoGroup.VideoGroupListVO;
import com.abdecd.moebackend.business.service.VideoGroupService;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Tag(name = "通用视频组接口")
@Slf4j
@RestController
@RequestMapping("video-group")
public class VideoGroupController {
    @Resource
    private VideoGroupService videoGroupService;

    @Resource
    private VideoService videoService;

    @Operation(summary = "视频组类型获取", description = "data字段返回视频组类型")
    @GetMapping("/type")
    public Result<Integer> getVideoGroupType(@Valid @RequestParam("videoGroupId") String videoGroupId){
        Integer videoGroupType =   videoGroupService.getTypeByVideoId(Long.valueOf(videoGroupId));
        return Result.success(videoGroupType);
    }

    @Operation(summary = "视频组列表获取", description = "data字段返回视频组列表")
    @GetMapping("/list")
    public Result<VideoGroupListVO> getVideoGroupList(@Valid @RequestParam("page") Integer page, @Valid @RequestParam("pageSize") Integer pageSize){
        VideoGroupListVO videoGroupListVO = videoGroupService.getVideoGroupList(page,pageSize);
        return Result.success(videoGroupListVO);
    }

    @Operation(summary = "视频组对应视频获取", description = "data字段返回视频组对应视频")
    @GetMapping("/list-all-video")
    public Result<ArrayList<VideoCompleteVO>> getAllVideo(@Valid @RequestParam("videoGroupId") Integer videoGroupId){
        ArrayList<VideoCompleteVO> videoCompleteVOArrayList = new ArrayList<>();
        ArrayList<Video> videoList = videoService.getVideoListByGid(videoGroupId);

        for(Video video : videoList){
            VideoCompleteVO videoCompleteVO = new VideoCompleteVO();

            videoCompleteVO.setId(video.getId());
            videoCompleteVO.setTitle(video.getTitle());
            videoCompleteVO.setDescription(video.getDescription());
            videoCompleteVO.setCover(video.getCover());
            videoCompleteVO.setVideoGroupId(Long.valueOf(videoGroupId));
            videoCompleteVO.setIndex(video.getIndex());
            videoCompleteVO.setLink(video.getLink());

            videoCompleteVOArrayList.add(videoCompleteVO);
        }
        return  Result.success(videoCompleteVOArrayList);
    }
}
