package com.hit.compiler.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitProblemItemOutput implements Serializable {

  private String message;

  private String output;

  private String test_case;

  private String error;

  private String expectedOutput;

}
