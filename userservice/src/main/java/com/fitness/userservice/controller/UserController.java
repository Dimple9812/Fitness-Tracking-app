package com.fitness.userservice.controller;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor  // if any const. added below ,it will be automatically wired
public class UserController {

    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId) {   //user response-DTO i.e., data transfer obj. that will represent the response
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping ("/register")  //post cz sending data and creating a resource
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }
    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {   //user response-DTO i.e., data transfer obj. that will represent the response
        return ResponseEntity.ok(userService.existByUserId(userId));
    }
}
