package com.abdecd.moebackend.business.pojo.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploaderVO
{
    private Long id;
    private String nickname;
    private String avatar;
}
