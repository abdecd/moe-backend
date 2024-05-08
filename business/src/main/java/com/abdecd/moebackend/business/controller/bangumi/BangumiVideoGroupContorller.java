package com.abdecd.moebackend.business.controller.bangumi;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.dao.mapper.BangumiVideoGroupMapper;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "番剧视频组接口")
@Slf4j
@RestController
@RequestMapping("bangumi-video-group")
public class BangumiVideoGroupContorller {
    @Resource
    private VIdeoGroupService videoGroupService;

    @Resource
    private BangumiVideoGroupMapper bangumiVideoGroupMapper;

    @RequestMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    public Result<Long> addBangumiVideoGroup(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO){
        Long vid = videoGroupService.insert(bangumiVideoGroupAddDTO);

        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();
        bangumiVideoGroup.setVideo_group_id(vid);
        bangumiVideoGroup.setUpdate_at_announcement(bangumiVideoGroupAddDTO.getUpdateAtAnnouncement());
        bangumiVideoGroup.setRelease_time(bangumiVideoGroupAddDTO.getReleaseTime());
        //TODO 默认值修改
        bangumiVideoGroup.setStatus(1);

        bangumiVideoGroupMapper.insert(bangumiVideoGroup);

        return Result.success(vid);
    }

    @PostMapping(value = "/delete")
    //TODO 删缓存
    public Result deleteBangumiVideoGroup(Long id)
    {
        videoGroupService.delete(id);
        bangumiVideoGroupMapper.deleteByVid(id);
        return Result.success();
    }
}
