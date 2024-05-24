package com.abdecd.moebackend.business.service.video;

import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoForceVO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;

import java.util.ArrayList;

public interface VideoService {
    long addVideo(AddVideoDTO addVideoDTO, Byte videoStatusWillBe);
//    long addVideoWithCoverResolved(AddVideoDTO addVideoDTO);
    boolean checkVideoPending(Long videoId);
    void updateVideo(UpdateVideoDTO updateVideoDTO);
    void deleteVideo(Long videoId);
    VideoVO getVideo(Long videoId);
    VideoForceVO getVideoForce(Long videoId);
    void videoStatusUpdate(Long videoId, Byte videoStatus);

    ArrayList<Video> getVideoListByGid(Long videoGroupId);
}
