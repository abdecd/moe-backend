package com.abdecd.moebackend.business.pojo.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddReportDTO {
    @NotNull
    private Integer type;

    @NotNull
    private Long targetId;

    @NotBlank
    private String reason;
}
