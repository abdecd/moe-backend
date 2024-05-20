package com.abdecd.moebackend.business.pojo.vo.videogroup;

import com.abdecd.moebackend.business.dao.entity.SearchVideoGroupEntity;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideoGroupVO {
    private Long id;
    private String title;
    private UploaderVO uploader;
    private String cover;
    private String description;
    private String tags;
    private Byte type;
    private LocalDateTime createTime;
    private Byte videoGroupStatus;
    @JsonIgnore
    private Double weight;

    public SearchVideoGroupEntity toSearchNovelEntity() {
        var obj = new SearchVideoGroupEntity();
        obj.setId(id);
        obj.setTitle(title);
        obj.setUploaderName(uploader.getNickname());
        obj.setDescription(description);
        obj.setTags(List.of(tags.split(";")));
        obj.setType(type);
        obj.setYear(String.valueOf(createTime.getYear()));
        if (this instanceof BangumiVideoGroupVO bVO) obj.setYear(String.valueOf(bVO.getReleaseTime().getYear()));
        obj.refreshSuggestion();
        obj.setWeight(weight);
        return obj;
    }
}
