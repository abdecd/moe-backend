package com.abdecd.moebackend.business.pojo.dto.recommend;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SetCarouselDTO {
    @NotNull
    private long[] ids;
}
