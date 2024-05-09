package com.abdecd.moebackend.business.controller.bangumi;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.BangumiVideoGroupMapper;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.service.BangumiVideoGroupSever;
import com.abdecd.moebackend.business.service.VIdeoGroupService;
import com.abdecd.moebackend.business.service.impl.BangumiVideoGroupSeverlmpl;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<Long> addBangumiVideoGroup(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO){
        Long vid = videoGroupService.insert(bangumiVideoGroupAddDTO);

        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();
        bangumiVideoGroup.setVideoGroupId(vid);
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupAddDTO.getUpdateAtAnnouncement());
        bangumiVideoGroup.setReleaseTime(bangumiVideoGroupAddDTO.getReleaseTime());
        //TODO 默认值修改
        bangumiVideoGroup.setStatus(1);

        bangumiVideoGroupSever.insert(bangumiVideoGroup);

        return Result.success(vid);
    }

    @PostMapping(value = "/delete")
    //TODO 删缓存
    public Result deleteBangumiVideoGroup(Long id)
    {
        videoGroupService.delete(id);
        bangumiVideoGroupSever.deleteByVid(id);
        return Result.success();
    }

    @RequestMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    public  Result updateBangumiVideoGroup(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO){

        videoGroupService.update(bangumiVideoGroupUpdateDTO);
        bangumiVideoGroupSever.update(bangumiVideoGroupUpdateDTO);
        return Result.success();
    }

    @GetMapping("")
    public  Result<BangumiVideoGroupVO> getBangumiVideoGroupInfo(@RequestParam("id") Long id){
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        bangumiVideoGroupVO.setVideoGroupId(id);

        bangumiVideoGroupVO = videoGroupService.getByVideoId(bangumiVideoGroupVO.getVideoGroupId());
        bangumiVideoGroupVO = bangumiVideoGroupSever.getByVid(bangumiVideoGroupVO);
        return Result.success(bangumiVideoGroupVO);
    }
}
