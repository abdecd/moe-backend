package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.dao.mapper.VideoGroupMapper;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.result.PageVO;
import com.abdecd.moebackend.common.result.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "搜索")
@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private VideoGroupMapper videoGroupMapper;
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;

    // todo
    @Operation(summary = "搜索")
    @GetMapping("")
    public Result<PageVO<VideoGroupWithDataVO>> search(
            @RequestParam String q,
            @RequestParam(required = false) Byte type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        var pageObj = new Page<VideoGroup>(page, pageSize);
        var ids = videoGroupMapper.selectPage(pageObj, new LambdaQueryWrapper<VideoGroup>()
                .select(VideoGroup::getId, VideoGroup::getTitle)
                .eq(type != null, VideoGroup::getType, type)
                .like(VideoGroup::getTitle, q)
        );
        var pageVO = new PageVO<VideoGroupWithDataVO>()
                .setTotal((int) ids.getTotal())
                .setRecords(ids.getRecords().stream()
                        .map(id -> videoGroupServiceBase.getVideoGroupWithData(id.getId()))
                        .toList()
                );
        return Result.success(pageVO);
    }

    @Operation(summary = "获得搜索建议")
    @GetMapping("suggestion")
    public Result<List<String>> getSuggestion(
            @RequestParam String q,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(15) Integer num
    ) {
        var titles = videoGroupMapper.selectList(new LambdaQueryWrapper<VideoGroup>()
                .select(VideoGroup::getTitle)
                .like(VideoGroup::getTitle, q)
                .last("limit "+num)
        );
        return Result.success(titles.stream().map(VideoGroup::getTitle).toList());
    }
}
