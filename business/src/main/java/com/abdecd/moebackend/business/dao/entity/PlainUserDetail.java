package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PlainUserDetail {
    private Long userId;
    private String avatar;
    private String signature;
}
