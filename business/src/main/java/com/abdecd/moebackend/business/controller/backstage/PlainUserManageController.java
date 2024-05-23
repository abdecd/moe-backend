package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.pojo.dto.plainuser.BanUserDTO;
import com.abdecd.moebackend.business.pojo.vo.plainuser.AllVO;
import com.abdecd.moebackend.business.service.PlainUserManage.PlainUserManageService;
import com.abdecd.moebackend.common.result.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/backstage/plain-user-manage")
public class PlainUserManageController {

    @Autowired
    private PlainUserManageService userService;

    @Operation(summary = "列出所有用户")
    @GetMapping("/list")
    public Result<Object> listUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<AllVO> userPage = userService.listUsers(id, name, status, page, pageSize);
        return Result.success(Map.of(
                "total", userPage.getTotal(),
                "records", userPage.getRecords()
        ));
    }

    @Operation(summary = "封禁/解封用户")
    @PostMapping("/ban")
    public Result<String> banUser(@RequestBody @Valid BanUserDTO banUserDTO) {
        boolean success = userService.banUser(banUserDTO);
        if (success) {
            return Result.success("ok");
        } else {
            return Result.error("用户处理失败");
        }
    }
}
