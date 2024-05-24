package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.bangumiVideoGroup.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoVo;
import com.abdecd.moebackend.business.pojo.vo.statistic.StatisticDataVO;
import com.abdecd.moebackend.business.service.backstage.BangumiVideoGroupService;
import com.abdecd.moebackend.business.service.backstage.VideoGroupService;
import com.abdecd.moebackend.business.service.statistic.StatisticService;
import com.abdecd.moebackend.common.result.PageVO;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Tag(name = "后台番剧视频组接口")
@Slf4j
@RestController
@RequestMapping("/backstage/bangumi-video-group")
public class BangumiVideoGroupController {
    @Resource
    private VideoGroupService videoGroupService;

    @Resource
    private BangumiVideoGroupService bangumiVideoGroupService;
    @Resource
    private StatisticService statisticService;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "番剧视频组添加", description = "data字段返回新增视频组id")
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    @ResponseBody
    public Result<Long> addBangumiVideoGroup(@Valid BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now.format(formatter);

        Long vid = bangumiVideoGroupService.insert(bangumiVideoGroupAddDTO);

        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();
        bangumiVideoGroup.setVideoGroupId(vid);
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupAddDTO.getUpdateAtAnnouncement());
        bangumiVideoGroup.setReleaseTime(bangumiVideoGroupAddDTO.getReleaseTime());
        bangumiVideoGroup.setStatus(Integer.valueOf(bangumiVideoGroupAddDTO.getStatus()));
        bangumiVideoGroup.setUpdateTime(now);

        bangumiVideoGroupService.insert(bangumiVideoGroup);

        return Result.success(vid);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "番剧视频组删除")
    @PostMapping(value = "/delete")
    public Result<String> deleteBangumiVideoGroup(@Valid Long id) {
//        videoGroupService.delete(id);
        bangumiVideoGroupService.deleteByVid(id);
//        videoGroupAndTagService.deleteByVideoGroupId(id);
        videoGroupService.deleteVideoGroup(id);
        return Result.success();
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "番剧视频组更新")
    @PostMapping(value = "/update", consumes = "multipart/form-data")
    @ResponseBody
    public Result<String> updateBangumiVideoGroup(@Valid BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO) {
        bangumiVideoGroupService.update(bangumiVideoGroupUpdateDTO);
        videoGroupService.update(bangumiVideoGroupUpdateDTO);
//        videoGroupAndTagService.update(bangumiVideoGroupUpdateDTO);
        return Result.success();
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "番剧视频组获取", description = "data字段返回番剧视频组信息")
    @GetMapping("")
    public Result<BangumiVideoGroupVO> getBangumiVideoGroupInfo(@Valid @RequestParam("id") Long id) {
        BangumiVideoGroupVO bangumiVideoGroupVO = new BangumiVideoGroupVO();
        bangumiVideoGroupVO.setId(String.valueOf(id));

        bangumiVideoGroupVO = bangumiVideoGroupService.getByVideoId(Long.valueOf(bangumiVideoGroupVO.getId()));
        BangumiVideoGroupVO bangumiVideoGroupVO_ = bangumiVideoGroupService.getByVid(Long.valueOf(bangumiVideoGroupVO.getId()));

        bangumiVideoGroupVO.setReleaseTime(bangumiVideoGroupVO_.getReleaseTime());
        bangumiVideoGroupVO.setUpdateAtAnnouncement(bangumiVideoGroupVO_.getUpdateAtAnnouncement());
        bangumiVideoGroupVO.setStatus(bangumiVideoGroupVO_.getStatus());

        StatisticDataVO statisticDataVO = statisticService.getStatisticData(id);
        bangumiVideoGroupVO.setWatchCnt(Math.toIntExact(statisticDataVO.getWatchCnt()));
        bangumiVideoGroupVO.setFavoriteCnt(Math.toIntExact(statisticDataVO.getFavoriteCnt()));
        bangumiVideoGroupVO.setLikeCnt(Math.toIntExact(statisticDataVO.getLikeCnt()));
        bangumiVideoGroupVO.setUserLike(statisticDataVO.getUserLike());
        bangumiVideoGroupVO.setUserFavorite(statisticDataVO.getUserFavorite());
        bangumiVideoGroupVO.setCommentCnt(Math.toIntExact(statisticDataVO.getCommentCnt()));
        bangumiVideoGroupVO.setDanmakuCnt(Math.toIntExact(statisticDataVO.getDanmakuCnt()));

        return Result.success(bangumiVideoGroupVO);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "番剧视频组目录获取", description = "data字段返回番剧视频组目录")
    @GetMapping("/contents")
    public Result<ArrayList<VideoVo>> getBangumiVideoGroupContent(@Valid @RequestParam("id") Long id) {
        ArrayList<VideoVo> videoVoList = videoGroupService.getContentById(id);
        return Result.success(videoVoList);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "获取所有符合条件的番剧视频组", description = "data字段返回番剧视频组信息")
    @GetMapping("/all")
    public Result<PageVO<com.abdecd.moebackend.business.pojo.vo.videogroup.BangumiVideoGroupVO>> getAllBangumiVideoGroup(
            @Valid @Nullable @RequestParam("pageIndex") Integer pageIndex,
            @Valid @Nullable @RequestParam("pageSize") Integer pageSize,
            @Valid @Nullable @RequestParam("id") String id,
            @Valid @Nullable @RequestParam("title") String title,
            @Valid @Nullable @RequestParam("status") Byte status) {
        pageIndex = pageIndex == null ? 1 : pageIndex;
        pageSize = pageSize == null ? 10 : pageSize;

        var list = bangumiVideoGroupService.getBangumiVideoGroupList((pageIndex - 1) * pageSize, pageSize, id, title, status);
        var total = bangumiVideoGroupService.getBangumiVideoGroupListCount(id, title, status);
        return Result.success(new PageVO<com.abdecd.moebackend.business.pojo.vo.videogroup.BangumiVideoGroupVO>().setRecords(list).setTotal(total));
    }

}
