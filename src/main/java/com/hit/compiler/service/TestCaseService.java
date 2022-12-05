package com.hit.compiler.service;

import com.hit.compiler.dto.TestCaseDto;
import com.hit.compiler.entity.TestCase;

import java.util.List;

public interface TestCaseService {
  List<TestCase> getAllTestCase();

  TestCase getTestCaseById(Long id);

  TestCase createTestCase(Long problemId, TestCaseDto testCaseDto);

  TestCase editTestCase(Long id, TestCaseDto testCaseDto);

  TestCase deleteTestCase(Long id);
}
