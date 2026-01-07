package com.alok.postapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class UserController {

    @GetMapping("profile")
    public ResponseEntity<String> getUserProfile() {
        return ResponseEntity.ok("Helloooooooooooooooo it is your profile");
    }
}
