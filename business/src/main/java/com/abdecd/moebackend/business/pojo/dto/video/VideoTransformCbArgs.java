package com.abdecd.moebackend.business.pojo.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VideoTransformCbArgs {
    private String taskId;
    private Type type;
    private Status status;

    public enum Type {
        VIDEO_TRANSFORM
    }

    public enum Status {
        SUCCESS,
        FAIL
    }
}
