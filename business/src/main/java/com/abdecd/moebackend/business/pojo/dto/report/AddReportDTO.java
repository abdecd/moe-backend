package com.abdecd.moebackend.business.pojo.dto.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddReportDTO {
    @NotNull
    @Min(0)
    @Max(1)
    private Integer type;

    @NotNull
    private Long targetId;

    @NotBlank
    private String reason;
}
