package com.hit.compiler.service.imp;

import com.hit.compiler.dto.UserDto;
import com.hit.compiler.entity.User;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.payload.TrueFalseResponse;
import com.hit.compiler.repository.UserRepository;
import com.hit.compiler.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    }
    throw new VsException("Can not find user by id = " + id);
  }

  @Override
  public User createUser(UserDto userDto) {
    if (userRepository.findByUsername(userDto.getUsername()) != null) {
      throw new VsException("Duplicate username: " + userDto.getUsername());
    }
    User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getFullName(),
        userDto.getGender(), userDto.getBirthday());
    return userRepository.save(user);
  }

  @Override
  public User editUser(Long id, UserDto userDto) {
    User user = getUserById(id);
    user.setFullName(userDto.getFullName());
    user.setGender(userDto.getGender());
    user.setBirthday(userDto.getBirthday());
    return userRepository.save(user);
  }

  @Override
  public TrueFalseResponse deleteUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    userRepository.deleteById(id);
    return new TrueFalseResponse(user.isPresent());
  }
}
