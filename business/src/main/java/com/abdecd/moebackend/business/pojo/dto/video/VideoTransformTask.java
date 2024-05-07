package com.abdecd.moebackend.business.pojo.dto.video;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class VideoTransformTask {
    private String id;
    private Long videoId;
    private String originPath;
    private String targetPath;
    private Status status;
    /**
     * 回调函数名称 例如 videoServiceImpl.addVideo
     * @回调函数需要有唯一参数VideoTransformCbStatus
     */
    private String cbBeanNameAndMethodName;

    public enum Status {
        WAITING,
        SUCCESS
    }
}
