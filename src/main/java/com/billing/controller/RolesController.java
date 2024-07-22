package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("roles")
public class RolesController {

    @GetMapping
    public String roles() {
        return "views/roles/roles";
    }


}
