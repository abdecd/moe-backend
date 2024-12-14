package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.vo.bangumiindex.HotTags;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.BangumiIndexService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "番剧索引接口")
@RestController
@RequestMapping("/video-group")
public class BangumiIndexController {
    @Autowired
    private BangumiIndexService bangumiIndexService;

    @Operation(summary = "获取热门tags用来搜索")
    @GetMapping("hot-tags")
    public Result<List<HotTags>> getHotTags() {
        return Result.success(bangumiIndexService.getHotTags());
    }

    @Operation(summary = "番剧索引", description = "最多返回50条")
    @GetMapping("bangumi-index")
    public Result<List<VideoGroupWithDataVO>> listBangumi(Byte type) {
        return Result.success(bangumiIndexService.listBangumi(type));
    }
}
