package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;

public interface VideoService {
    long addVideo(AddVideoDTO addVideoDTO);
    boolean checkAddVideoPending(Long videoId);
}
