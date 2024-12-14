package com.abdecd.moebackend.business.controller.backstage;

import com.abdecd.moebackend.business.exceptionhandler.BaseException;
import com.abdecd.moebackend.business.pojo.dto.feedback.DeleteFeedbackDTO;
import com.abdecd.moebackend.business.pojo.dto.feedback.HandleFeedbackDTO;
import com.abdecd.moebackend.business.pojo.vo.feedback.FeedbackVO;
import com.abdecd.moebackend.business.service.feedback.FeedbackService;
import com.abdecd.moebackend.business.tokenLogin.aspect.RequirePermission;
import com.abdecd.moebackend.common.result.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequirePermission(value = "99", exception = BaseException.class)
@RestController
@RequestMapping("/backstage/feedback")
public class FeedbackControllerBack {

    @Autowired
    private FeedbackService feedbackService;

    @Operation(summary = "查看反馈")
    @GetMapping
    public Result<Object> getFeedbacks(
            @NotNull Integer page,
            @NotNull Integer pageSize,
            String email,
            String content) {

        Page<FeedbackVO> feedbackPage = feedbackService.getFeedbacks(page, pageSize, email, content);
        return Result.success(Map.of(
                "total", feedbackPage.getTotal(),
                "records", feedbackPage.getRecords()
        ));
    }

    @Operation(summary = "处理反馈")
    @PostMapping("/handle")
    public Result<String> handleFeedback(@RequestBody @Valid HandleFeedbackDTO handleFeedbackDTO) {
        boolean success = feedbackService.handleFeedback(handleFeedbackDTO);
        if (success) {
            return Result.success("ok");
        } else {
            return Result.error("反馈处理失败");
        }
    }

    @Operation(summary = "删除反馈")
    @PostMapping("/delete")
    public Result<String> deleteFeedback(@RequestBody @Valid DeleteFeedbackDTO dto) {
        feedbackService.deleteFeedback(dto.getIds());
        return Result.success("ok");
    }
}
