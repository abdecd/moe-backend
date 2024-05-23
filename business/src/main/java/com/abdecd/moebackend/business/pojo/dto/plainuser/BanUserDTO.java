package com.abdecd.moebackend.business.pojo.dto.plainuser;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BanUserDTO {
    @NotNull
    private Long id;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;
}
