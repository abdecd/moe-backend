package com.abdecd.moebackend.business.pojo.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VideoTransformCbArgs {
    private String taskId;
    private VideoTransformTask.TaskType type;
    private Status status;

    public enum Status {
        SUCCESS,
        FAIL
    }
}
