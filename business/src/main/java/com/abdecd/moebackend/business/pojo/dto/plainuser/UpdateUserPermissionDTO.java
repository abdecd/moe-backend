package com.abdecd.moebackend.business.pojo.dto.plainuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateUserPermissionDTO {
    @NotNull
    private Long id;

    @NotBlank
    private String permission;
}
