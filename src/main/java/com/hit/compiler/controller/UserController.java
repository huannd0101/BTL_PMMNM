package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV1;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.dto.UserDto;
import com.hit.compiler.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  public ResponseEntity<?> getAllUser() {
    return VsResponseUtil.ok(userService.getAllUser());
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(userService.getUserById(id));
  }

  @PostMapping("/users")
  public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
    return VsResponseUtil.ok(userService.createUser(userDto));
  }

  @PatchMapping("/users/{id}")
  public ResponseEntity<?> getAllUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
    return VsResponseUtil.ok(userService.editUser(id, userDto));
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(userService.deleteUserById(id));
  }

}
