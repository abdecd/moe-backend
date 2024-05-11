package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoVo;
import com.abdecd.moebackend.business.service.BangumiVideoGroupServer;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Tag(name = "番剧视频组接口")
@Slf4j
@RestController
@RequestMapping("bangumi-video-group")
public class BangumiVideoGroupContorller {
    @Resource
    private VIdeoGroupService videoGroupService;

    @Autowired
    private BangumiVideoGroupServer bangumiVideoGroupServer;

    @Operation(summary = "番剧视频组添加", description = "data字段返回新增视频组id")
    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    public Result<Long> addBangumiVideoGroup(@Valid  BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO){
        Long vid = bangumiVideoGroupServer.insert(bangumiVideoGroupAddDTO);


        Integer status = bangumiVideoGroupAddDTO.getStatus().equals("已完结")?1:0;

        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();
        bangumiVideoGroup.setVideoGroupId(vid);
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupAddDTO.getUpdateAtAnnouncement());
        bangumiVideoGroup.setReleaseTime(bangumiVideoGroupAddDTO.getReleaseTime());
        bangumiVideoGroup.setStatus(status);

        bangumiVideoGroupServer.insert(bangumiVideoGroup);

        return Result.success(vid);
    }

    @Operation(summary = "番剧视频组删除")
    @PostMapping(value = "/delete")
    @CacheEvict(value = "bangumiVideoGroup",key = "#id")
    public Result deleteBangumiVideoGroup(@Valid Long id)
    {
        videoGroupService.delete(id);
        bangumiVideoGroupServer.deleteByVid(id);
        return Result.success();
    }

    @Operation(summary = "番剧视频组更新")
    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    @CacheEvict(value = "bangumiVideoGroup",key = "#bangumiVideoGroupUpdateDTO.id")
    public  Result updateBangumiVideoGroup(@Valid BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO){
        bangumiVideoGroupServer.update(bangumiVideoGroupUpdateDTO);
        bangumiVideoGroupServer.update(bangumiVideoGroupUpdateDTO);
        return Result.success();
    }

    @Operation(summary = "番剧视频组获取", description = "data字段返回番剧视频组信息")
    @GetMapping("")
    public  Result<BangumiVideoGroupVO> getBangumiVideoGroupInfo(@Valid @RequestParam("id") Long id){
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        bangumiVideoGroupVO.setVideoGroupId(id);

        bangumiVideoGroupVO = bangumiVideoGroupServer.getByVideoId(bangumiVideoGroupVO.getVideoGroupId());
        bangumiVideoGroupVO = bangumiVideoGroupServer.getByVid(bangumiVideoGroupVO);
        return Result.success(bangumiVideoGroupVO);
    }

    @Operation(summary = "番剧视频组目录获取", description = "data字段返回番剧视频组目录")
    @GetMapping("/contentsContent")
    public Result<ArrayList<VideoVo>> getBangumiVideoGroupContent(@Valid @RequestParam("id") Long id){
        ArrayList<VideoVo> videoVoList = videoGroupService.getContentById(id);
        return Result.success(videoVoList);
    }
}
