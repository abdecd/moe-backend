package com.abdecd.moebackend.business.controller.backstage;


import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.pojo.dto.backstage.report.ReportDTO;
import com.abdecd.moebackend.business.pojo.vo.report.ReportCommentTotalVO;
import com.abdecd.moebackend.business.pojo.vo.report.ReportVideoTotalVO;
import com.abdecd.moebackend.business.service.report.ReportService;
import com.abdecd.moebackend.common.result.Result;
import com.abdecd.tokenlogin.aspect.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backstage/report")
public class ReportBackController {

    @Autowired
    private ReportService reportService;

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "审核举报")
    @PostMapping("/audit")
    public Result<String> auditReport(@Valid @RequestBody ReportDTO reportDTO) {
        reportService.auditReport(reportDTO.getId());
        return Result.success();
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "查看视频举报")
    @GetMapping("/video")
    public Result<ReportVideoTotalVO> getVideoReport(@Valid @RequestParam("page") Integer page, @Valid @RequestParam("pageSize") Integer pageSize) {
        ReportVideoTotalVO reportVideoTotalVO = reportService.getReportVideoVO(page, pageSize);
        return Result.success(reportVideoTotalVO);
    }

    @RequirePermission(value = "99", exception = BaseException.class)
    @Operation(summary = "查看评论举报")
    @GetMapping("/comment")
    public Result<ReportCommentTotalVO> getCommentReport(@Valid @RequestParam("page") Integer page, @Valid @RequestParam("pageSize") Integer pageSize) {
        ReportCommentTotalVO reportCommentTotalVO = reportService.getReportCommentVO(page, pageSize);
        return Result.success(reportCommentTotalVO);
    }
}
