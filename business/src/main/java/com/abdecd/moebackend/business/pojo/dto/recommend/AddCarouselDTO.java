package com.abdecd.moebackend.business.pojo.dto.recommend;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AddCarouselDTO {
    @NotNull
    @Min(0)
    private int index;
    @NotNull
    private long[] ids;
}
