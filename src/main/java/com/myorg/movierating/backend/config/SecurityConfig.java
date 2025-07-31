package com.myorg.movierating.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    private final AzureB2CProperties azureB2CProperties;

    public SecurityConfig(AzureB2CProperties azureB2CProperties) {
        this.azureB2CProperties = azureB2CProperties;
    }

    @Bean
    @Profile("!test") // Only use in non-test profiles
    public SecurityFilterChain filterChain(final HttpSecurity http, final ClientRegistrationRepository
            clients) throws Exception {

        String logoutRedirectUri = UriUtils.encode(azureB2CProperties.getLogoutRedirectUri(), StandardCharsets.UTF_8);
        //  Configure OIDC logout handler for Azure CIAM
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clients);
        //  Set the post-logout redirect URI (after Azure B2C logs the user out)
        //  This should match the front-channel logout URL registered in Azure
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(azureB2CProperties.getLogoutRedirectUri());

        http
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/", "/login", "/index", "/movies/best-picture-owner", "/movies/top-10-rated-movies", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/ratings/rate-movies", "/ratings/submit-ratings").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        //  Always redirect here upon successful login
                        .defaultSuccessUrl("/", true)
                )
                //  Logout configuration
                .logout(logout -> logout

                        .logoutSuccessHandler(oidcLogoutSuccessHandler)         //  Redirects to Azure CIAM logout endpoint
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll())
                .build();
    }


}
