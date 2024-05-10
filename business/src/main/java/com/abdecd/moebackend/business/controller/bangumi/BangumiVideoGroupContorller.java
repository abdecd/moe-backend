package com.abdecd.moebackend.business.controller.bangumi;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.BangumiVideoGroupMapper;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoVo;
import com.abdecd.moebackend.business.service.BangumiVideoGroupSever;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.business.service.impl.BangumiVideoGroupSeverlmpl;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    private BangumiVideoGroupSever bangumiVideoGroupSever;

    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    public Result<Long> addBangumiVideoGroup(@Valid  BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO){
        Long vid = videoGroupService.insert(bangumiVideoGroupAddDTO);


        Integer status = bangumiVideoGroupAddDTO.getStatus().equals("已完结")?1:0;

        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();
        bangumiVideoGroup.setVideoGroupId(vid);
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupAddDTO.getUpdateAtAnnouncement());
        bangumiVideoGroup.setReleaseTime(bangumiVideoGroupAddDTO.getReleaseTime());
        bangumiVideoGroup.setStatus(status);

        bangumiVideoGroupSever.insert(bangumiVideoGroup);

        return Result.success(vid);
    }

    @PostMapping(value = "/delete")
    @CacheEvict(value = "bangumiVideoGroup",key = "#id")
    public Result deleteBangumiVideoGroup(@Valid Long id)
    {
        videoGroupService.delete(id);
        bangumiVideoGroupSever.deleteByVid(id);
        return Result.success();
    }

    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    @CacheEvict(value = "bangumiVideoGroup",key = "#bangumiVideoGroupUpdateDTO.id")
    public  Result updateBangumiVideoGroup(@Valid BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO){
        videoGroupService.update(bangumiVideoGroupUpdateDTO);
        bangumiVideoGroupSever.update(bangumiVideoGroupUpdateDTO);
        return Result.success();
    }

    @GetMapping("")
    @Cacheable(value = "bangumiVideoGroup",key = "#id")
    public  Result<BangumiVideoGroupVO> getBangumiVideoGroupInfo(@Valid @RequestParam("id") Long id){
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        bangumiVideoGroupVO.setVideoGroupId(id);

        bangumiVideoGroupVO = videoGroupService.getByVideoId(bangumiVideoGroupVO.getVideoGroupId());
        bangumiVideoGroupVO = bangumiVideoGroupSever.getByVid(bangumiVideoGroupVO);
        return Result.success(bangumiVideoGroupVO);
    }

    @GetMapping("/contentsContent")
    @Cacheable(value = "bangumiVideoGroup",key = "#id")
    public Result<ArrayList<VideoVo>> getBangumiVideoGroupContent(@Valid @RequestParam("id") Long id){
        ArrayList<VideoVo> videoVoList = videoGroupService.getContentById(id);
        return Result.success(videoVoList);
    }
}
