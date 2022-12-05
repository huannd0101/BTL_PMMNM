package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV0;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.dto.UserDto;
import com.hit.compiler.payload.AuthenticationRequest;
import com.hit.compiler.payload.AuthenticationResponse;
import com.hit.compiler.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestApiV0
public class AuthV0Controller {
  private final AuthService authService;

  public AuthV0Controller(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto) {
    AuthenticationResponse output = authService.signup(userDto);

    return VsResponseUtil.ok(output);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
    return VsResponseUtil.ok(authService.login(authenticationRequest));
  }

}
