package com.abdecd.moebackend.business.controller.base;

import com.abdecd.moebackend.business.pojo.dto.feedback.AddFeedbackDTO;
import com.abdecd.moebackend.business.service.feedback.FeedbackService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Operation(summary = "添加反馈")
    @PostMapping("/add")
    public Result<String> addFeedback(@RequestBody @Valid AddFeedbackDTO addFeedbackDTO) {
        Long feedbackId = feedbackService.addFeedback(addFeedbackDTO);
        return Result.success(feedbackId + "");
    }
}
