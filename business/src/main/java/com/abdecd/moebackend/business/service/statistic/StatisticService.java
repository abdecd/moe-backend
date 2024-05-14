package com.abdecd.moebackend.business.service.statistic;

import com.abdecd.moebackend.business.common.util.SpringContextUtil;
import com.abdecd.moebackend.business.pojo.dto.statistic.VideoPlayDTO;
import com.abdecd.moebackend.business.pojo.vo.statistic.StatisticDataVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.ContentsItemVO;
import com.abdecd.moebackend.business.service.CommentService;
import com.abdecd.moebackend.business.service.DanmakuService;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {
    @Autowired
    private LastWatchTimeStatistic lastWatchTimeStatistic;
    @Autowired
    private TotalWatchTimeStatistic totalWatchTimeStatistic;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DanmakuService danmakuService;

    public void cntVideoPlay(VideoPlayDTO videoPlayDTO, int addTime) {
        // 记录上次观看位置
        lastWatchTimeStatistic.add(videoPlayDTO.getVideoId(), videoPlayDTO.getWatchTime());
        // 记录播放总时长
        totalWatchTimeStatistic.add(videoPlayDTO.getVideoId(), addTime);
        // 统计播放量
        // todo 统计未登录用户
        stringRedisTemplate.opsForHyperLogLog().add(
                RedisConstant.STATISTIC_VIDEO_PLAY_CNT + videoPlayDTO.getVideoId(),
                UserContext.getUserId() + ""
        );
    }

    public StatisticDataVO getStatisticData(Long videoGroupId) {
        // 获取播放量
//        Long watchCnt = stringRedisTemplate.opsForHyperLogLog().size(
//                RedisConstant.STATISTIC_VIDEO_PLAY_CNT + videoGroupId
//        );
        Long watchCnt = (long) (Math.random()*1000000);
        // todo
        // 获取点赞量
        Long likeCnt = (long) (Math.random()*100000);
        // 获取收藏量
        Long favoriteCnt = (long) (Math.random()*100000);
        // 获取评论量
        var videoGroupServiceBase = SpringContextUtil.getBean(VideoGroupServiceBase.class);
        List<ContentsItemVO> videoContents = (List<ContentsItemVO>) videoGroupServiceBase.getContents(videoGroupId);
        if (videoContents == null) videoContents = new ArrayList<>();
        Long commentCnt = videoContents.stream().map(x->commentService.getCommentCount(x.getVideoId())).reduce(0L, Long::sum);
        Long danmakuCnt = videoContents.stream().map(x->danmakuService.getDanmakuCount(x.getVideoId())).reduce(0L, Long::sum);
        return new StatisticDataVO()
                .setWatchCnt(watchCnt)
                .setLikeCnt(likeCnt)
                .setFavoriteCnt(favoriteCnt)
                .setCommentCnt(commentCnt)
                .setDanmakuCnt(danmakuCnt);
    }
}
