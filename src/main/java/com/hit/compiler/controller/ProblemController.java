package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV1;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.dto.EditProblem;
import com.hit.compiler.dto.ProblemDto;
import com.hit.compiler.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestApiV1
public class ProblemController {
  private final ProblemService problemService;

  public ProblemController(ProblemService problemService) {
    this.problemService = problemService;
  }

  @GetMapping("/problem")
  public ResponseEntity<?> getAllProblem() {
    return VsResponseUtil.ok(problemService.getAllProblem());
  }

  @GetMapping("/problem/{id}")
  public ResponseEntity<?> getProblemById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(problemService.getProblemById(id));
  }

  @PostMapping("/problem")
  public ResponseEntity<?> createProblem(@RequestBody ProblemDto problemDto) throws IOException {
    return VsResponseUtil.ok(problemService.createProblem(problemDto));
  }

  @PatchMapping("/problem/{id}")
  public ResponseEntity<?> editProblemById(@PathVariable("id") Long id, @RequestBody EditProblem editProblem) {
    return VsResponseUtil.ok(problemService.editProblem(id, editProblem));
  }

  @DeleteMapping("/problem/{id}")
  public ResponseEntity<?> deleteProblemById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(problemService.deleteProblem(id));
  }

}
