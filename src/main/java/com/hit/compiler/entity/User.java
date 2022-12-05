package com.hit.compiler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;

  @Nationalized
  private String fullName;

  private String gender;

  private String birthday;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id")
  private Role role;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private List<ContestUser> contestUsers;

  public User(String username, String password, String fullName, String gender, String birthday) {
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.gender = gender;
    this.birthday = birthday;
  }

  public User(String username, String password, String fullName, String gender, String birthday, Role role) {
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.gender = gender;
    this.birthday = birthday;
    this.role = role;
  }
}
