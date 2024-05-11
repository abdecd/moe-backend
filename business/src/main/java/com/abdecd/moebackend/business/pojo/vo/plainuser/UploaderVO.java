package com.abdecd.moebackend.business.pojo.vo.plainuser;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UploaderVO {
    private Long id;
    private String avatar;
    private String nickname;
}
