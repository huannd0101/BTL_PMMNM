package com.hit.compiler.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hit.compiler.constant.CommonConstant;
import com.hit.compiler.dto.*;
import com.hit.compiler.entity.Contest;
import com.hit.compiler.entity.Problem;
import com.hit.compiler.entity.TestCase;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.output.InitProblemExternalOutput;
import com.hit.compiler.output.InitProblemOutput;
import com.hit.compiler.output.SubmitProblemOutput;
import com.hit.compiler.repository.ContestRepository;
import com.hit.compiler.repository.ProblemRepository;
import com.hit.compiler.repository.TestCaseRepository;
import com.hit.compiler.service.ProblemService;
import com.hit.compiler.util.FileUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProblemServiceImp implements ProblemService {
  private final ContestRepository contestRepository;
  private final ProblemRepository problemRepository;
  private final TestCaseRepository testCaseRepository;
  private final ObjectMapper objectMapper;

  public ProblemServiceImp(ContestRepository contestRepository, ProblemRepository problemRepository,
                           TestCaseRepository testCaseRepository, ObjectMapper objectMapper) {
    this.contestRepository = contestRepository;
    this.problemRepository = problemRepository;
    this.testCaseRepository = testCaseRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<Problem> getAllProblem() {
    return problemRepository.findAll();
  }

  @Override
  public Problem getProblemById(Long id) {
    Optional<Problem> problem = problemRepository.findById(id);
    if (problem.isEmpty()) {
      throw new VsException("Can not find problem by id " + id);
    }
    return problem.get();
  }

  @Override
  public Problem createProblem(ProblemDto problemDto) throws IOException {
    Optional<Contest> contest = contestRepository.findById(problemDto.getContestId());
    if (contest.isEmpty()) {
      throw new VsException("Can not find contest by id " + problemDto.getContestId());
    }
    Problem problem = problemRepository.save(new Problem(problemDto.getTitle(), problemDto.getContent(),
        problemDto.getTotalPoint(), problemDto.getTimeLimit(), problemDto.getMemoryLimit(), contest.get()));
    List<TestCase> testCases = new ArrayList<>();
    for (TestCaseDto testCaseDto : problemDto.getLstTestCase()) {
      testCases.add(testCaseRepository.save(new TestCase(testCaseDto.getInput(), testCaseDto.getOutput(), problem)));
    }
    problem.setTestCases(testCases);

    // Call external API
    List<File> inputs = new ArrayList<>();
    Map<String, String> outputs = new HashMap<>();
    int i = 1;
    for (TestCaseDto testCaseDto : problemDto.getLstTestCase()) {
      //output
      outputs.put(String.valueOf(i), testCaseDto.getOutput());
      //input
      File testcase = FileUtil.writeToFile(testCaseDto.getInput(), "in" + i++ + ".txt");
      inputs.add(testcase);
    }

    File problemFile = new File("problem.zip");
    FileUtil.zipFile(inputs, problemFile);

    Map<String, Object> payloads = new HashMap<>();
    payloads.put("memoryLimit", problemDto.getMemoryLimit());
    payloads.put("timeLimit", problemDto.getTimeLimit());
    payloads.put("input", new FileSystemResource(problemFile));
    payloads.put("output", objectMapper.writeValueAsString(outputs));

    Object res = ExternalAPICommon.post(
        CommonConstant.COMPILER_URL + "/init",
        payloads,
        String.class,
        true
    );

    InitProblemExternalOutput resCallExternalAPI = objectMapper.readValue((String) res,
        InitProblemExternalOutput.class);

    problem.setCompilerId(resCallExternalAPI.getData());

    return problemRepository.save(problem);
  }


  private SubmitProblemOutput submit(File source, String lang, String id, String userId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("source", new FileSystemResource(source));
    body.add("lang", lang);
    body.add("id", id);
    body.add("userId", userId);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.postForEntity(CommonConstant.COMPILER_URL, requestEntity,
        SubmitProblemOutput.class).getBody();
  }

//  private InitProblemOutput initProblem(Long memoryLimit, Long timeLimit, File input, Map<String, String> output) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//    body.add("memoryLimit", memoryLimit);
//    body.add("timeLimit", timeLimit);
//    body.add("input", new FileSystemResource(input));
//    body.add("output", output);
//
//    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//    RestTemplate restTemplate = new RestTemplate();
//    return restTemplate.postForEntity(CommonConstant.COMPILER_URL + "/init", requestEntity,
//        InitProblemOutput.class).getBody();
//  }

  @Override
  public Problem editProblem(Long id, EditProblem editProblem) {
    Problem problem = getProblemById(id);
    problem.setTitle(editProblem.getTitle());
    problem.setContent(editProblem.getContent());
    problem.setMemoryLimit(editProblem.getMemoryLimit());
    problem.setTotalPoint(editProblem.getTotalPoint());
    problem.setTimeLimit(editProblem.getTimeLimit());
    return problemRepository.save(problem);
  }

  @Override
  public Problem deleteProblem(Long id) {
    Problem problem = getProblemById(id);
    for (TestCase testCase : problem.getTestCases()) {
      testCaseRepository.delete(testCase);
    }
    problemRepository.delete(problem);
    return problem;
  }
}
