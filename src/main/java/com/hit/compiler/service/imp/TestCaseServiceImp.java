package com.hit.compiler.service.imp;

import com.hit.compiler.dto.TestCaseDto;
import com.hit.compiler.entity.Problem;
import com.hit.compiler.entity.TestCase;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.repository.ProblemRepository;
import com.hit.compiler.repository.TestCaseRepository;
import com.hit.compiler.service.TestCaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestCaseServiceImp implements TestCaseService {
  private final ProblemRepository problemRepository;
  private final TestCaseRepository testCaseRepository;

  public TestCaseServiceImp(ProblemRepository problemRepository, TestCaseRepository testCaseRepository) {
    this.problemRepository = problemRepository;
    this.testCaseRepository = testCaseRepository;
  }

  @Override
  public List<TestCase> getAllTestCase() {
    return testCaseRepository.findAll();
  }

  @Override
  public TestCase getTestCaseById(Long id) {
    Optional<TestCase> testCase = testCaseRepository.findById(id);
    if (testCase.isEmpty()) {
      throw new VsException("Can not find testcase id = " + id);
    }
    return testCase.get();
  }

  @Override
  public TestCase createTestCase(Long problemId, TestCaseDto testCaseDto) {
    Optional<Problem> optional = problemRepository.findById(problemId);
    if (optional.isEmpty()) {
      throw new VsException("Can not find problem by id " + problemId);
    }
    Problem problem = optional.get();
    TestCase testCase = new TestCase(testCaseDto.getInput(), testCaseDto.getOutput(), problem);

    return testCaseRepository.save(testCase);
  }

  @Override
  public TestCase editTestCase(Long id, TestCaseDto testCaseDto) {
    Optional<TestCase> optional = testCaseRepository.findById(testCaseDto.getTestCaseId());
    if (optional.isEmpty()) {
      throw new VsException("Can not find testcase by id " + testCaseDto.getTestCaseId());
    }
    TestCase testCase = optional.get();
    testCase.setInput(testCaseDto.getInput());
    testCase.setOutput(testCaseDto.getOutput());

    return testCaseRepository.save(testCase);
  }

  @Override
  public TestCase deleteTestCase(Long id) {
    Optional<TestCase> optional = testCaseRepository.findById(id);
    if (optional.isEmpty()) {
      throw new VsException("Can not find testcase by id " + id);
    }
    testCaseRepository.delete(optional.get());
    return optional.get();
  }
}
