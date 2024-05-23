package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.pojo.dto.recommend.SetCarouselDTO;
import com.abdecd.moebackend.business.service.RecommendService;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "推荐接口")
@RestController
@RequestMapping("/backstage/video-group")
public class RecommendControllerBack {
    @Autowired
    private RecommendService recommendService;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "设置轮播列表")
    @PostMapping("carousel")
    public Result<String> setCarousel(@RequestBody @Valid SetCarouselDTO setCarouselDTO) {
        recommendService.setCarouselIds(setCarouselDTO.getIds());
        return Result.success();
    }
}
