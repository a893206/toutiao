package com.cr.toutiao.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用配置
 *
 * @author cr
 * @date 2022/10/19 23:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    /**
     * 域名
     */
    private String domain;
}
