package com.hit.compiler.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class DataUtil {

  public static String getPassword(String password) {
    PasswordEncoder passwordEncoder = BeanUtil.getBean(PasswordEncoder.class);
    return passwordEncoder.encode(password);
  }

}
