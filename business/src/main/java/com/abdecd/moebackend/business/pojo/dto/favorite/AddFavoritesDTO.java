package com.abdecd.moebackend.business.pojo.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddFavoritesDTO {
    @NotNull
    private long videoGroupId;
}
