package com.sumui.common.model.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WXConstant {
    private String appid;
    private String secret;
    private String url;
    private String accessTokenUrl;
}
