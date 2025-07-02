package com.backbase.movierating.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "azure.b2c")
public class AzureB2CProperties {
    private String tenantId;
    private String tenantName;
    private String policy;
    private String logoutRedirectUri;
}

