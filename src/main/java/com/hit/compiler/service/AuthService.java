package com.hit.compiler.service;

import com.hit.compiler.dto.UserDto;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.payload.AuthenticationRequest;
import com.hit.compiler.payload.AuthenticationResponse;

public interface AuthService {

  AuthenticationResponse login(AuthenticationRequest request) throws VsException;

  AuthenticationResponse signup(UserDto userDto);

}