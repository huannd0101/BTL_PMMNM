package com.hit.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestDto {

  private String name;

  private String description;

  private Long startTime;

  private Long endTime;

  private String password;

  private String status;

}
