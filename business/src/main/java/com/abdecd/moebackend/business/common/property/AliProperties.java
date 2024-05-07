package com.abdecd.moebackend.business.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ali.oss")
@Data
public class AliProperties {
    String bucketName;
    String accessKeyId;
    String accessKeySecret;
    String endpoint;
    String stsEndpoint;
    String stsRegionId;
    String stsRoleArn;
    Long stsDurationSeconds;
    Long stsMaxSize;
    String watermark;
}
