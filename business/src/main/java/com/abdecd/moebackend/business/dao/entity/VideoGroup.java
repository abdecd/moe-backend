package com.abdecd.moebackend.business.dao.entity;
import io.micrometer.common.lang.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;


@Accessors(chain = true)
@Data
public class VideoGroup {
    @Nullable
    private Long id;
    private Long userId;
    private String title;
    private String cover;
    private String description;
    private LocalTime create_time;
    private Integer type;
    private Double weight;
}
