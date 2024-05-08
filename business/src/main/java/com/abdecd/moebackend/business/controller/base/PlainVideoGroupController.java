package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoVo;
import com.abdecd.moebackend.business.service.VIdeoGroupAndTagService;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Tag(name = "普通视频组接口")
@Slf4j
@RestController
@RequestMapping("plain-video-group")
public class PlainVideoGroupController {

    @Resource
    private VIdeoGroupService videoGroupService;

    @Resource
    private VIdeoGroupAndTagService videoGroupAndTagService; ;

    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    //TODO 加个缓存
    public Result<Long> addVideoGroup(VIdeoGroupDTO videoGroupDTO)
    {
        //TODO 校验数据是不是都有
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String date = dtf.format(ldt);
        videoGroupDTO.setDate(date);

        Long gorupId = videoGroupService.insert(videoGroupDTO);

        for(String i : videoGroupDTO.getTagIds())
        {
            Long tagId = Long.valueOf(i);
            videoGroupAndTagService.insert(tagId,gorupId);
        }

        return Result.success(gorupId);
    }

    @PostMapping(value = "/delete")
    //TODO 删缓存
    public Result delVideoGroup(@RequestParam("id") Long id)
    {
        videoGroupService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    //TODO 删缓存
    public Result updateVideoGroup(VIdeoGroupDTO videoGroupDTO)
    {
        VideoGroupVO videoGroupVO = videoGroupService.update(videoGroupDTO);
        return Result.success();
    }

    @GetMapping("")
    //TODO 加个缓存
    public Result<VideoGroupVO> getVideoGroup(@RequestParam("id") Long id)
    {
        VideoGroupVO videoGroupVO = videoGroupService.getById(id);
        return Result.success(videoGroupVO);
    }

    @GetMapping("/contents")
    public Result<ArrayList<VideoVo>>getVideoGroupContent(@RequestParam("id") Long id){
        ArrayList<VideoVo> videoVoList = videoGroupService.getContentById(id);
        return Result.success(videoVoList);
    }
}
