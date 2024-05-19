package com.abdecd.moebackend.business.pojo.dto.foreground;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlainVideoGroupAddDTO {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String tags;

    @NotNull
    private String cover;

    @NotNull
    private String link;
}
