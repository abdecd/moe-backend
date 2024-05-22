package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.pojo.dto.plainuser.UpdatePlainUserDTO;
import com.abdecd.moebackend.business.service.plainuser.PlainUserService;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "普通用户接口")
@RestController
@RequestMapping("/plain-user")
public class PlainUserController {
    @Autowired
    private PlainUserService plainUserService;

    @Operation(summary = "获取用户信息")
    @GetMapping("")
    public Result<PlainUserDetail> getUserInfo(Long uid) {
        if (uid == null) uid = UserContext.getUserId();
        return Result.success(plainUserService.getPlainUserDetail(uid));
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("update")
    public Result<String> updateUserInfo(@Valid UpdatePlainUserDTO updatePlainUserDTO) {
        plainUserService.updatePlainUserDetail(updatePlainUserDTO);
        return Result.success();
    }
}

