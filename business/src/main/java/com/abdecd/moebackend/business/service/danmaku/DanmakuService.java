package com.abdecd.moebackend.business.service.danmaku;

import com.abdecd.moebackend.business.pojo.dto.danmaku.AddDanmakuDTO;
import com.abdecd.moebackend.business.pojo.vo.danmaku.DanmakuVO;

import java.time.LocalDateTime;
import java.util.List;

public interface DanmakuService {
    Long addDanmaku(AddDanmakuDTO addDanmakuDTO);
    List<DanmakuVO> getDanmaku(Long videoId, Integer segmentIndex);
    void deleteDanmaku(Long id);
    Long getDanmakuCount(Long videoId);
    LocalDateTime getDanmakuTimestamp(Long videoId, Integer segmentIndex);
    void updateDanmakuTimestamp(Long videoId);
}
