package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV1;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.dto.TestCaseDto;
import com.hit.compiler.service.TestCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class TestCaseController {
  private final TestCaseService testCaseService;

  public TestCaseController(TestCaseService testCaseService) {
    this.testCaseService = testCaseService;
  }

  @GetMapping("/testcase")
  public ResponseEntity<?> getAllTestCase() {
    return VsResponseUtil.ok(testCaseService.getAllTestCase());
  }

  @GetMapping("/testcase/{id}")
  public ResponseEntity<?> getTestCaseById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(testCaseService.getTestCaseById(id));
  }

  @PostMapping("/testcase/{problemId}")
  public ResponseEntity<?> createTestCase(@PathVariable("problemId") Long problemId,
                                          @RequestBody TestCaseDto testCaseDto) {
    return VsResponseUtil.ok(testCaseService.createTestCase(problemId, testCaseDto));
  }

  @PatchMapping("/testcase/{id}")
  public ResponseEntity<?> editTestCaseById(@PathVariable("id") Long id, @RequestBody TestCaseDto testCaseDto) {
    return VsResponseUtil.ok(testCaseService.editTestCase(id, testCaseDto));
  }

  @DeleteMapping("/testcase/{id}")
  public ResponseEntity<?> deleteTestCaseById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(testCaseService.deleteTestCase(id));
  }

}
