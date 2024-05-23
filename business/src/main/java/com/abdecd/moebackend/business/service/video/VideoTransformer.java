package com.abdecd.moebackend.business.service.video;

import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformTask;

import java.util.concurrent.CompletableFuture;

public interface VideoTransformer {
    CompletableFuture<Void> transform(VideoTransformTask task, int ttlSeconds, String username);
}
