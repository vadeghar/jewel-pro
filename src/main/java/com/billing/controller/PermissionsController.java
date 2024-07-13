package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("permissions")
public class PermissionsController {

    @GetMapping
    @RequestMapping("{id}")
    public String get(@PathVariable(required = false) Long id) {
        return "views/permissions/permission";
    }

    @PostMapping
    public String post() {
        return "views/permissions/permission";
    }

    @GetMapping
    public String list() {
        return "views/permissions/permission-list";
    }
}
