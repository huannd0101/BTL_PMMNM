package com.hit.compiler.service.imp;

import com.hit.compiler.dto.UserDto;
import com.hit.compiler.entity.Role;
import com.hit.compiler.entity.User;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.payload.AuthenticationRequest;
import com.hit.compiler.payload.AuthenticationResponse;
import com.hit.compiler.repository.RoleRepository;
import com.hit.compiler.repository.UserRepository;
import com.hit.compiler.service.AuthService;
import com.hit.compiler.service.MyUserDetailsService;
import com.hit.compiler.util.JwtUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImp implements AuthService {
  private final JwtUtil jwtUtil;
  private final MyUserDetailsService myUserDetailsService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public AuthServiceImp(JwtUtil jwtUtil, MyUserDetailsService myUserDetailsService,
                        AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                        UserRepository userRepository, RoleRepository roleRepository) {
    this.jwtUtil = jwtUtil;
    this.myUserDetailsService = myUserDetailsService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public AuthenticationResponse login(AuthenticationRequest request) throws VsException {
    com.hit.compiler.entity.User user = userRepository.findByUsername(request.getUsername());
    try {
      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          request.getUsername(), request.getPassword()
      ));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (BadCredentialsException e) {
      if (ObjectUtils.isEmpty(user)) {
        throw new VsException("Incorrect username");
      }
      throw new VsException("Wrong password");
    }

    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(request.getUsername(),
        request.getPassword()));

    return new AuthenticationResponse(jwt, user.getId(), user.getUsername(), user.getRole().getName());
  }

  @Override
  public AuthenticationResponse signup(UserDto userDto) {
    //validate and get setting
    User oldUser = userRepository.findByUsername(userDto.getUsername());
    if (oldUser != null) {
      throw new VsException("Username has already exists");
    }

    User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getFullName(),
        userDto.getGender(), userDto.getBirthday());

    Role role = roleRepository.findByName("ROLE_USER");
    user.setRole(role);
    User newUser = userRepository.save(user);
    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());

    return new AuthenticationResponse(jwtUtil.generateToken(userDetails), newUser.getId(),
        newUser.getUsername(), newUser.getRole().getName());
  }

}