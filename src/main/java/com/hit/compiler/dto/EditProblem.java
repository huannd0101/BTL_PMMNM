package com.hit.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditProblem {

  private String title;

  private String content;

  private Long totalPoint;

  private Long timeLimit;

  private Long memoryLimit;


}
