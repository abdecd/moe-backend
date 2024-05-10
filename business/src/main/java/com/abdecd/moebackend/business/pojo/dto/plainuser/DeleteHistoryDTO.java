package com.abdecd.moebackend.business.pojo.dto.plainuser;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteHistoryDTO {
    @NotNull
    long[] videoGroupIds;
}
