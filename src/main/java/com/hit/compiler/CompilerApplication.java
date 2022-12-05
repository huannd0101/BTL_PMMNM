package com.hit.compiler;

import com.hit.compiler.entity.Role;
import com.hit.compiler.entity.User;
import com.hit.compiler.repository.RoleRepository;
import com.hit.compiler.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CompilerApplication {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public CompilerApplication(UserRepository userRepository, RoleRepository roleRepository,
                             PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public static void main(String[] args) {
    SpringApplication.run(CompilerApplication.class, args);
  }

  @Bean
  CommandLineRunner init() {
    return args -> {
      if (roleRepository.count() == 0) {
        roleRepository.save(new Role("ROLE_ADMIN", "Role admin"));
        roleRepository.save(new Role("ROLE_USER", "Role user"));
      }
      if (userRepository.count() == 0) {
        userRepository.save(
            new User("admin", passwordEncoder.encode("admin"), "admin", "", "01-01-2001",
                roleRepository.findByName("ROLE_ADMIN")));
        userRepository.save(
            new User("user", passwordEncoder.encode("user"), "user", "", "01-01-2001",
                roleRepository.findByName("ROLE_USER"))
        );
      }
    };
  }

}
