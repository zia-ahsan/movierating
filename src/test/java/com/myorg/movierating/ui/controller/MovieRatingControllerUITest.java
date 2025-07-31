package com.myorg.movierating.ui.controller;

import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.service.MovieRatingService;
import com.myorg.movierating.backend.service.MovieService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SuppressWarnings("removal")
@WebMvcTest(MovieRatingControllerUI.class)
//@AutoConfigureMockMvc(addFilters = false)   // Disable security for test
@TestPropertySource(properties = "spring.profiles.active=test")
class MovieRatingControllerUITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieRatingService movieRatingService;

    @Test
    @DisplayName("GET /ratings/rate-movies should return form view with moviePage and ratings")
    void shouldShowRatingForm() throws Exception {
        // Mock OIDC claims
        Map<String, Object> claims = Map.of(
                "sub", "test@user.com",
                "email", "test@user.com"
        );

        // Create an OIDC token with claims
        OidcIdToken idToken = new OidcIdToken(
                "dummy-token",
                Instant.now(),
                Instant.now().plusSeconds(60),
                claims
        );

        // Create OIDC user with authorities
        OidcUser oidcUser = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                idToken
        );

        // Wrap in an authentication token
        Authentication auth = new OAuth2AuthenticationToken(
                oidcUser,
                oidcUser.getAuthorities(),
                "oidc"
        );

        // Mock movie
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setYear(2010);
        Page<Movie> moviePage = new PageImpl<>(List.of(movie));

        // Mock service returns
        when(movieService.getAllMovies(PageRequest.of(0, 10))).thenReturn(moviePage);
        when(movieRatingService.getRatingsByUser("test@user.com"))
                .thenReturn(Map.of(1L, 5));

        // Perform the request
        mockMvc.perform(get("/ratings/rate-movies").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("rate-movies"))
                .andExpect(model().attributeExists("moviePage"))
                .andExpect(model().attributeExists("ratings"));

        // Verify mocks
        verify(movieService).getAllMovies(PageRequest.of(0, 10));
        verify(movieRatingService).getRatingsByUser("test@user.com");
    }

    @Disabled
    @Test
    @DisplayName("POST /ratings/submit-ratings should save ratings and redirect")
    void shouldSubmitRatings() throws Exception {
        // Mock OidcUser
        OidcUser mockOidcUser = mock(OidcUser.class);
        when(mockOidcUser.getEmail()).thenReturn("test@user.com");

        // Wrap mockOidcUser inside an Authentication mock
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(mockOidcUser);

        // Fake movie returned by service
        Movie mockMovie = new Movie();
        mockMovie.setId(1L);
        mockMovie.setTitle("Inception");
        mockMovie.setYear(2010);
        when(movieService.getMovieById(1L)).thenReturn(mockMovie);

        // Perform POST
        mockMvc.perform(post("/ratings/submit-ratings")
                        .param("ratings[1]", "5")
                        .param("movieIds[1]", "1")
                        .with(csrf()) // Add CSRF token
                        .principal(mockAuth))  // correct injection
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ratings/rate-movies"));

        verify(movieRatingService).saveRatings(anyList(), eq(mockAuth));
    }



}
