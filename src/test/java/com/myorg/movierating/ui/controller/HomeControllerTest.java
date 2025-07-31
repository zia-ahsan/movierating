package com.myorg.movierating.ui.controller;

import com.myorg.movierating.backend.config.AzureB2CProperties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("removal")
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AzureB2CProperties azureB2CProperties;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Disabled
    @Test
    void shouldRenderHomePageWithOAuthToken() {
        // Mock OAuth2User
        OAuth2User principal = mock(OAuth2User.class);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Test User");
        attributes.put("emails", new String[]{"test@example.com"}); // ✅ Must be String[]

        when(principal.getAttributes()).thenReturn(attributes);

        // Mock token
        OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(principal);

        // Inject into controller
        AzureB2CProperties props = mock(AzureB2CProperties.class);
        HomeController controller = new HomeController(props);

        // Use mock model
        Model model = mock(Model.class);

        // Act
        String result = controller.home(model, token);

        // Assert
        verify(model).addAttribute("userName", "Test User");
        verify(model).addAttribute("userEmail", "test@example.com");
        assertEquals("index", result);
    }


}
