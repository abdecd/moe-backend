package com.abdecd.moebackend.business.pojo.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
@Accessors(chain = true)
public class VideoGroupVO {
    private Long videoGroupId;
    private String title;
    private String description;
    private String cover;
    private UploaderVO uploader;
    private ArrayList<TagVO> tags;
}

@Data
@Accessors(chain = true)
class UploaderVO
{
    private Long id;
    private String nickname;
    private String avatar;
}

@Data
@Accessors(chain = true)
class TagVO
{
    private Long id;
    private String tagName;
}
