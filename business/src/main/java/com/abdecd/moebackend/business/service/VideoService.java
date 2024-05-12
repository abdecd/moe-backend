package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;

import java.util.ArrayList;

public interface VideoService {
    long addVideo(AddVideoDTO addVideoDTO);
    long addVideoWithCoverResolved(AddVideoDTO addVideoDTO);
    boolean checkVideoPending(Long videoId);
    void updateVideo(UpdateVideoDTO updateVideoDTO);
    void deleteVideo(Long videoId);
    VideoVO getVideo(Long videoId);

    ArrayList<Video> getVideoListByGid(Long videoGroupId);
}
