package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.favorite.AddFavoritesDTO;
import com.abdecd.moebackend.business.pojo.dto.favorite.DeleteFavoritesDTO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.FavoriteService;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收藏接口")
@RestController
@RequestMapping("/plain-user/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "获取用户收藏列表")
    @GetMapping("")
    public Result<List<VideoGroupWithDataVO>> getFavorites() {
        return Result.success(favoriteService.get(UserContext.getUserId()));
    }

    @Operation(summary = "获取用户是否收藏特定视频组")
    @GetMapping("contains")
    public Result<Boolean> getExists(@RequestParam Long videoGroupId) {
        return Result.success(favoriteService.exists(UserContext.getUserId(), videoGroupId));
    }

    @Operation(summary = "删除用户收藏")
    @PostMapping("delete")
    public Result<String> deleteFavorites(@RequestBody @Valid DeleteFavoritesDTO deleteFavoritesDTO) {
        favoriteService.delete(UserContext.getUserId(), deleteFavoritesDTO.getVideoGroupIds());
        return Result.success();
    }

    @Operation(summary = "添加用户收藏")
    @PostMapping("add")
    public Result<String> addFavorites(@RequestBody @Valid AddFavoritesDTO addFavoritesDTO) {
        favoriteService.add(UserContext.getUserId(), addFavoritesDTO.getVideoGroupId());
        return Result.success();
    }
}
