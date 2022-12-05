package com.hit.compiler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "problem_contest_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemContestResult {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long code;

  private String status;

  private Long totalTime;

  private Long totalPoint;

  private String source;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "problem_id")
  private Problem problem;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "contest_user_id")
  private ContestUser contestUser;

  public ProblemContestResult(Long code, String status, Long totalTime, Long totalPoint, String source,
                              Problem problem, ContestUser contestUser) {
    this.code = code;
    this.status = status;
    this.totalTime = totalTime;
    this.totalPoint = totalPoint;
    this.source = source;
    this.problem = problem;
    this.contestUser = contestUser;
  }
}
