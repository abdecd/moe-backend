package com.abdecd.moebackend.business.pojo.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AliStsVO {
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String endpoint;
}
