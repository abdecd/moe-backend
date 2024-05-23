package com.abdecd.moebackend.business.pojo.vo.bangumiindex;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class HotTags {
    private String title;
    private List<Object> tags;
}
