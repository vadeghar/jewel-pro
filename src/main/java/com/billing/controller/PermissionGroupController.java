package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/permission-groups")
public class PermissionGroupController {

    @GetMapping
    public String get(@RequestParam(required = false, name = "id") Long id) {
        return "views/permission-group/permission-group";
    }
    @GetMapping("list")
    public String list() {
        return "views/permission-group/permission-group-list";
    }

    @PostMapping
    public String post() {
        return "views/permissions/permission";
    }


}

