package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupVO;
import com.abdecd.moebackend.business.service.VIdeoGroupAndTagService;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "普通视频组接口")
@Slf4j
@RestController
@RequestMapping("plain-video-group")
public class CommonVideoGroupController {

    @Resource
    private VIdeoGroupService videoGroupService;

    @Resource
    private VIdeoGroupAndTagService videoGroupAndTagService; ;

    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
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
    public Result delVideoGroup(@RequestParam("id") Long id)
    {
        videoGroupService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    public Result updateVideoGroup(VIdeoGroupDTO videoGroupDTO)
    {
        VideoGroupVO videoGroupVO = videoGroupService.update(videoGroupDTO);
        return Result.success();
    }

    @GetMapping("")
    public Result<VideoGroupVO> getVideoGroup(@RequestParam("id") Long id)
    {
        VideoGroupVO videoGroupVO = videoGroupService.getById(id);
        return Result.success(videoGroupVO);
    }
}
