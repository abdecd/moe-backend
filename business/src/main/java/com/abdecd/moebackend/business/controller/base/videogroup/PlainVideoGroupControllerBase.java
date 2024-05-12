package com.abdecd.moebackend.business.controller.base.videogroup;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.videogroup.*;
import com.abdecd.moebackend.business.pojo.vo.videogroup.ContentsItemVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupVO;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.business.service.videogroup.PlainVideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "普通视频组接口")
@RestController
@RequestMapping("plain-video-group")
public class PlainVideoGroupControllerBase {
    @Autowired
    private PlainVideoGroupServiceBase plainVideoGroupServiceBase;
    @Autowired
    private VideoService videoService;

    @Operation(summary = "获取视频组信息")
    @GetMapping("")
    public Result<VideoGroupVO> getVideoGroup(@RequestParam Long id) {
        return Result.success(plainVideoGroupServiceBase.getVideoGroupInfo(id));
    }

    @Operation(summary = "获取视频组目录")
    @GetMapping("contents")
    public Result<List<ContentsItemVO>> getContents(@RequestParam Long id) {
        return Result.success(plainVideoGroupServiceBase.getContents(id));
    }

    @Operation(summary = "添加普通视频组综合接口")
    @PostMapping("add")
    @Transactional
    public Result<Long> addVideoGroup(@RequestBody PlainVideoGroupFullAddDTO addDTO) {
        var groupAddDTO = new PlainVideoGroupAddDTO();
        BeanUtils.copyProperties(addDTO, groupAddDTO);
        var videoGroupId = plainVideoGroupServiceBase.addVideoGroup(groupAddDTO);

        var videoAddDTO = new AddVideoDTO()
                .setVideoGroupId(videoGroupId)
                .setIndex(0)
                .setTitle(addDTO.getTitle())
                .setCover(addDTO.getCover())
                .setDescription(addDTO.getDescription())
                .setLink(addDTO.getLink());
        videoService.addVideo(videoAddDTO);
        return Result.success(videoGroupId);
    }

    @Operation(summary = "修改普通视频组综合接口")
    @PostMapping("update")
    @Transactional
    public Result<String> updateVideoGroup(@RequestBody PlainVideoGroupFullUpdateDTO updateDTO) {
        // todo 提前检验所有参数 防止非原子性
        var groupUpdateDTO = new PlainVideoGroupUpdateDTO();
        BeanUtils.copyProperties(updateDTO, groupUpdateDTO);
        plainVideoGroupServiceBase.updateVideoGroup(groupUpdateDTO);

        var videoGroupId = updateDTO.getId();
        var contents = plainVideoGroupServiceBase.getContents(videoGroupId);
        if (contents == null || contents.isEmpty()) throw new BaseException(MessageConstant.INVALID_VIDEO_GROUP);
        var videoId = contents.getFirst().getVideoId();

        var updateVideoDTO = new UpdateVideoDTO()
                .setId(videoId)
                .setVideoGroupId(videoGroupId)
                .setIndex(0)
                .setTitle(updateDTO.getTitle())
                .setCover(updateDTO.getCover())
                .setDescription(updateDTO.getDescription())
                .setLink(updateDTO.getLink());
        videoService.updateVideo(updateVideoDTO);
        return Result.success();
    }

    @Operation(summary = "删除普通视频组综合接口")
    @PostMapping("delete")
    public Result<String> deleteVideoGroup(@RequestBody @Valid PlainVideoGroupDeleteDTO deleteDTO) {
        plainVideoGroupServiceBase.deleteVideoGroup(deleteDTO.getId());
        return Result.success();
    }
}
