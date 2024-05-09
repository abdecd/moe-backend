package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.pojo.vo.common.VideoCompleteVO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupListVO;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
    private VIdeoGroupService videoGroupService;

    @Resource
    private VideoService videoService;

    @GetMapping("/type")
    public Result<Integer> getVideoGroupType(@RequestParam("videoGroupId") String videoGroupId){
        Integer videoGroupType =   videoGroupService.getTypeByVideoId(Long.valueOf(videoGroupId));
        return Result.success(videoGroupType);
    }

    @GetMapping("/list")
    public Result<VideoGroupListVO> getVideoGroupList(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize){
        VideoGroupListVO videoGroupListVO = videoGroupService.getVideoGroupList(page,pageSize);
        return Result.success(videoGroupListVO);
    }

    @GetMapping("/list-all-video")
    public Result<ArrayList<VideoCompleteVO>> getAllVideo(@RequestParam("videoGroupId") Integer videoGroupId){
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
