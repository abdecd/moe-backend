package com.abdecd.moebackend.business.controller.base;


import com.abdecd.moebackend.business.pojo.dto.report.AddReportDTO;

import com.abdecd.moebackend.business.service.report.ReportService;
import com.abdecd.moebackend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "添加举报")
    @PostMapping("/add")
    public Result<String> addReport(@RequestBody @Valid AddReportDTO addReportDTO) {
        Long reportId = reportService.addReport(addReportDTO);
        return Result.success();
    }
}
