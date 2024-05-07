package com.abdecd.moebackend.business.pojo.dto.video;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteVideoDTO {
    @NotNull
    Long id;
}
