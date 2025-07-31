package com.myorg.movierating.ui.controller;

import com.myorg.movierating.backend.config.AzureB2CProperties;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final AzureB2CProperties azureB2CProperties;

    @GetMapping("/")
    public String home(final Model model, final OAuth2AuthenticationToken token) {
        /*if (token != null) {
            String email = token.getPrincipal().getAttribute("email");
            String name = token.getPrincipal().getAttribute("name");

            model.addAttribute("userEmail", email);
            model.addAttribute("userName", name);
        }*/
        return "index";     // return Thymeleaf template "index.html"
    }

    @GetMapping("/login")
    public String login(final OAuth2AuthenticationToken token) {
        // If already logged in, redirect to home (or another page)
        if (token != null) {
            return "redirect:/";
        }
        // Not logged in: redirect to Azure B2C authorization endpoint to start login flow
        return "redirect:/oauth2/authorization/azure";
    }
}
