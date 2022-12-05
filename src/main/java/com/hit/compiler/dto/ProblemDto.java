package com.hit.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {

  private String title;

  private String content;

  private Long totalPoint;

  private Long timeLimit;

  private Long memoryLimit;

  private Long contestId;

  private List<TestCaseDto> lstTestCase;

}
