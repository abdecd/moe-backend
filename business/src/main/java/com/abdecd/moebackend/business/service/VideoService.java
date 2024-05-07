package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;

public interface VideoService {
    long addVideo(AddVideoDTO addVideoDTO);
    boolean checkVideoPending(Long videoId);
    void updateVideo(UpdateVideoDTO updateVideoDTO);
    void deleteVideo(Long videoId);
    VideoVO getVideo(Long videoId);
}
