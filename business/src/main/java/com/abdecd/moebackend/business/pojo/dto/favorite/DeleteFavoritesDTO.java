package com.abdecd.moebackend.business.pojo.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteFavoritesDTO {
    @NotNull
    long[] videoGroupIds;
}
