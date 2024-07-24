package com.billing.controller.rest;

import com.billing.dto.PasswordDto;
import com.billing.entity.Customer;
import com.billing.entity.User;
import com.billing.service.CustomerService;
import com.billing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/profile")
public class ProfileRestController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ProfileRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getProfile(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            user.setRoles(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }


    @PutMapping("/{username}")
    public ResponseEntity<User> updateProfile(@PathVariable String username, @RequestBody User profile) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            user.setUsername(profile.getUsername());
            user.setName(profile.getName());
            user.setEmail(profile.getEmail());
            user = userService.saveUser(user);
            user.setRoles(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/change-password/{username}")
    public ResponseEntity<PasswordDto> changePassword(@PathVariable String username, @RequestBody PasswordDto profile) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(profile.getPassword()));
            user = userService.saveUser(user);
            return ResponseEntity.ok(PasswordDto.builder().password(user.getPassword()).build());
        }
        return ResponseEntity.badRequest().build();
    }

}
