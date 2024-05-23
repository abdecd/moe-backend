package com.abdecd.moebackend.business.pojo.dto.plainuser;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BanUserDTO {
    @NotNull
    private Long id;

    @NotNull
    private Integer status;
}
