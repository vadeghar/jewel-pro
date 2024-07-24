package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @GetMapping
    public String profile() {
        return "views/profile/profile";
    }

    @GetMapping("change-password")
    public String changePassword() {
        return "views/profile/change-password";
    }
}
