package com.abdecd.moebackend.business.controller.base.videogroup;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.VideoMapper;
import com.abdecd.moebackend.business.lib.ResourceLinkHandler;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.videogroup.*;
import com.abdecd.moebackend.business.pojo.vo.videogroup.ContentsItemVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupVO;
import com.abdecd.moebackend.business.service.backstage.VideoGroupAndTagService;
import com.abdecd.moebackend.business.service.video.VideoService;
import com.abdecd.moebackend.business.service.videogroup.PlainVideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.VideoGroupConstant;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import com.abdecd.tokenlogin.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Tag(name = "普通视频组接口")
@RestController
@RequestMapping("plain-video-group")
public class PlainVideoGroupControllerBase {
    @Autowired
    private PlainVideoGroupServiceBase plainVideoGroupServiceBase;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ResourceLinkHandler resourceLinkHandler;
    @Autowired
    private VideoMapper videoMapper;
    @Resource
    VideoGroupAndTagService videoGroupAndTagService;

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

    @Operation(summary = "查看普通视频组处理状态")
    @GetMapping("check-pending")
    public Result<Boolean> checkAddVideoGroupPending(@RequestParam Long id) {
        return Result.success(plainVideoGroupServiceBase.checkAddVideoGroupPending(id));
    }

    @Operation(summary = "修改普通视频组综合接口")
    @PostMapping("update")
    @Transactional
    public Result<String> updateVideoGroup(@RequestBody PlainVideoGroupFullUpdateDTO updateDTO) {
        // 提前检验参数 防止非原子性
        if (updateDTO.getLink() != null) {
            var originPath = resourceLinkHandler.getRawPathFromTmpVideoLink(updateDTO.getLink());
            if (!originPath.startsWith("tmp/user" + UserContext.getUserId() + "/"))
                throw new BaseException(MessageConstant.INVALID_FILE_PATH);
            if (Objects.equals(videoMapper.selectById(updateDTO.getId()).getStatus(), Video.Status.TRANSFORMING))
                throw new BaseException(MessageConstant.VIDEO_TRANSFORMING);
        }

        var groupUpdateDTO = new PlainVideoGroupUpdateDTO();
        BeanUtils.copyProperties(updateDTO, groupUpdateDTO);
        plainVideoGroupServiceBase.updateVideoGroup(groupUpdateDTO, updateDTO.getLink() == null ? null : VideoGroup.Status.TRANSFORMING);

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

    /*@RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "普通视频组添加", description = "data字段返回新增普通视频组id")
    @PostMapping(value = "/add")
    @ResponseBody
    public Result<Long> addVideoGroup(@Valid com.abdecd.moebackend.business.pojo.dto.foreground.PlainVideoGroupAddDTO plainVideoGroupAddDTO) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();

        Long uid = UserContext.getUserId();

        Long groupId = foregroundPlainVideoGroupService.insert(new VideoGroup()
                .setCreateTime(ldt)
                .setUserId(uid)
                .setTitle(plainVideoGroupAddDTO.getTitle())
                .setDescription(plainVideoGroupAddDTO.getDescription())
                .setCover(plainVideoGroupAddDTO.getCover())
                .setType(VideoGroupConstant.COMMON_VIDEO_GROUP)
                .setWeight(VideoGroupConstant.DEFAULT_WEIGHT)
        );

        Long videoId = videoService.addVideo(new Video()
                .setVideoGroupId(groupId)
                .setLink(plainVideoGroupAddDTO.getLink())
                .setDescription(plainVideoGroupAddDTO.getDescription())
                .setTitle(plainVideoGroupAddDTO.getTitle())
                .setUploadTime(ldt)
        );

        videoGroupAndTagService.insertByTags(plainVideoGroupAddDTO.getTags(),groupId);

        return Result.success(groupId);
    }*/
}
