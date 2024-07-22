package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("permissions")
public class PermissionsController {

    @GetMapping
    public String get(@RequestParam(required = false, name = "id") Long id) {
        return "views/permissions/permission";
    }

    @PostMapping
    public String post() {
        return "views/permissions/permission";
    }

    @GetMapping("list")
    public String list() {
        return "views/permissions/permission-list";
    }
}
