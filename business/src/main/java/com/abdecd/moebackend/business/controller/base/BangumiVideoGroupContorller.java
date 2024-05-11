package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.bangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.bangumiVideoGroup.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.common.commonVideoGroup.VideoVo;
import com.abdecd.moebackend.business.service.BangumiVideoGroupServiceImpl;
import com.abdecd.moebackend.business.service.VideoGroupService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Tag(name = "番剧视频组接口")
@Slf4j
@RestController
@RequestMapping("bangumi-video-group")
public class BangumiVideoGroupContorller {
    @Resource
    private VideoGroupService videoGroupService;

    @Resource
    private BangumiVideoGroupServiceImpl bangumiVideoGroupServiceImpl;

    @Operation(summary = "番剧视频组添加", description = "data字段返回新增视频组id")
    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    public Result<Long> addBangumiVideoGroup(@Valid  BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO){
        Long vid = bangumiVideoGroupServiceImpl.insert(bangumiVideoGroupAddDTO);


        Integer status = bangumiVideoGroupAddDTO.getStatus().equals("已完结")?1:0;

        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();
        bangumiVideoGroup.setVideoGroupId(vid);
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupAddDTO.getUpdateAtAnnouncement());
        bangumiVideoGroup.setReleaseTime(bangumiVideoGroupAddDTO.getReleaseTime());
        bangumiVideoGroup.setStatus(status);

        bangumiVideoGroupServiceImpl.insert(bangumiVideoGroup);

        return Result.success(vid);
    }

    @Operation(summary = "番剧视频组删除")
    @PostMapping(value = "/delete")
    @CacheEvict(value = "bangumiVideoGroup",key = "#id")
    public Result<String> deleteBangumiVideoGroup(@Valid Long id)
    {
        videoGroupService.delete(id);
        bangumiVideoGroupServiceImpl.deleteByVid(id);
        return Result.success();
    }

    @Operation(summary = "番剧视频组更新")
    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    @CacheEvict(value = "bangumiVideoGroup",key = "#bangumiVideoGroupUpdateDTO.id")
    public  Result<String> updateBangumiVideoGroup(@Valid BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO){
        bangumiVideoGroupServiceImpl.update(bangumiVideoGroupUpdateDTO);
        bangumiVideoGroupServiceImpl.update(bangumiVideoGroupUpdateDTO);
        return Result.success();
    }

    @Operation(summary = "番剧视频组获取", description = "data字段返回番剧视频组信息")
    @GetMapping("")
    public  Result<BangumiVideoGroupVO> getBangumiVideoGroupInfo(@Valid @RequestParam("id") Long id){
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        bangumiVideoGroupVO.setVideoGroupId(id);

        bangumiVideoGroupVO = bangumiVideoGroupServiceImpl.getByVideoId(bangumiVideoGroupVO.getVideoGroupId());
        BangumiVideoGroupVO bangumiVideoGroupVO_ = bangumiVideoGroupServiceImpl.getByVid(bangumiVideoGroupVO.getVideoGroupId());

        bangumiVideoGroupVO.setReleaseTime(bangumiVideoGroupVO_.getReleaseTime());
        bangumiVideoGroupVO.setUpdateAtAnnouncement(bangumiVideoGroupVO_.getUpdateAtAnnouncement());
        bangumiVideoGroupVO.setStatus(bangumiVideoGroupVO_.getStatus());

        return Result.success(bangumiVideoGroupVO);
    }

    @Operation(summary = "番剧视频组目录获取", description = "data字段返回番剧视频组目录")
    @GetMapping("/contents")
    public Result<ArrayList<VideoVo>> getBangumiVideoGroupContent(@Valid @RequestParam("id") Long id){
        ArrayList<VideoVo> videoVoList = videoGroupService.getContentById(id);
        return Result.success(videoVoList);
    }
}
