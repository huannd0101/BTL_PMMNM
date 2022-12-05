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
@Table(name = "contests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contest {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nationalized
  private String name;

  @Nationalized
  private String description;

  private Long startTime;

  private Long endTime;

  private String password;

  @Nationalized
  private String status;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contest")
  @JsonIgnore
  private List<ContestUser> contestUsers;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contest")
  private List<Problem> problems;

  public Contest(String name, String description, Long startTime, Long endTime, String password, String status) {
    this.name = name;
    this.description = description;
    this.startTime = startTime;
    this.endTime = endTime;
    this.password = password;
    this.status = status;
  }
}
