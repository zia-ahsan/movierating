package com.myorg.movierating.backend.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AzureB2CPropertiesTest {

    @Test
    void testGettersAndSetters() {
        AzureB2CProperties props = new AzureB2CProperties();

        props.setTenantId("tenant123");
        props.setTenantName("tenantName");
        props.setPolicy("policyXYZ");
        props.setLogoutRedirectUri("http://logout.com");

        assertEquals("tenant123", props.getTenantId());
        assertEquals("tenantName", props.getTenantName());
        assertEquals("policyXYZ", props.getPolicy());
        assertEquals("http://logout.com", props.getLogoutRedirectUri());
    }
}
