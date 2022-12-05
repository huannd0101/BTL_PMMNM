package com.hit.compiler.service;

import com.hit.compiler.constant.UserMessageConstant;
import com.hit.compiler.entity.User;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public MyUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format("Can not find by username = %s", username));
    }

    Set<GrantedAuthority> grantedAuthorities =
        Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()));

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        grantedAuthorities);
  }

}
