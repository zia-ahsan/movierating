package com.myorg.movierating.ui.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserInfoAdvice {

    @ModelAttribute
    public void addUserInfoToModel(HttpServletRequest request, Model model) {
        Object auth = request.getUserPrincipal();
        if (auth instanceof OAuth2AuthenticationToken token) {
            String email = token.getPrincipal().getAttribute("email");
            String name = token.getPrincipal().getAttribute("name");

            model.addAttribute("userEmail", email);
            model.addAttribute("userName", name);
        }
    }
}

