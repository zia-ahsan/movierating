package com.myorg.movierating.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "azure.b2c")
public class AzureB2CProperties {
    private String tenantId;
    private String tenantName;
    private String policy;
    private String logoutRedirectUri;
}

