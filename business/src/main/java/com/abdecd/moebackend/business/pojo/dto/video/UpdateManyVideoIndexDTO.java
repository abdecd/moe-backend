package com.abdecd.moebackend.business.pojo.dto.video;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class UpdateManyVideoIndexDTO {
    @NotNull
    private List<UpdateVideoIndexDTO> arr;

    @Accessors(chain = true)
    @Data
    public static class UpdateVideoIndexDTO {
        @NotNull
        private Long videoId;
        @NotNull
        private Integer index;
    }
}
