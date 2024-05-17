package com.abdecd.moebackend.business.service.video;

import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformTask;

public interface VideoTransformer {
    void transform(VideoTransformTask task, int ttlSeconds, String username);
}
