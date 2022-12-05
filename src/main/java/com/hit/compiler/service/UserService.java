package com.hit.compiler.service;

import com.hit.compiler.dto.UserDto;
import com.hit.compiler.entity.User;
import com.hit.compiler.payload.TrueFalseResponse;

import java.util.List;

public interface UserService {

  List<User> getAllUser();

  User getUserById(Long id);

  User createUser(UserDto userDto);

  User editUser(Long id, UserDto userDto);

  TrueFalseResponse deleteUserById(Long id);

}
