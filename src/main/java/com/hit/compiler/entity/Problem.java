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
@Table(name = "problem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nationalized
  private String title;

  @Nationalized
  private String content;

  private Long totalPoint;

  private Long timeLimit;

  private Long memoryLimit;

  private String compilerId;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "contest_id")
  @JsonIgnore
  private Contest contest;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "problem")
  private List<TestCase> testCases;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "problem")
  @JsonIgnore
  private List<ProblemContestResult> problemContestResults;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "problem")
  @JsonIgnore
  private List<ProblemResult> problemResults;

  public Problem(String title, String content, Long totalPoint, Long timeLimit, Long memoryLimit, Contest contest) {
    this.title = title;
    this.content = content;
    this.totalPoint = totalPoint;
    this.timeLimit = timeLimit;
    this.memoryLimit = memoryLimit;
    this.contest = contest;
  }
}
