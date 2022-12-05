package com.hit.compiler.service.imp;

import com.hit.compiler.constant.CommonConstant;
import com.hit.compiler.entity.Problem;
import com.hit.compiler.entity.ProblemResult;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.input.SubmitProblemInput;
import com.hit.compiler.output.SubmitProblemItemOutput;
import com.hit.compiler.output.SubmitProblemOutput;
import com.hit.compiler.repository.ProblemContestResultRepository;
import com.hit.compiler.repository.ProblemRepository;
import com.hit.compiler.repository.ProblemResultRepository;
import com.hit.compiler.service.CompileService;
import com.hit.compiler.util.FileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompileServiceImp implements CompileService {
  private final ProblemRepository problemRepository;
  private final ProblemContestResultRepository problemContestResultRepository;
  private final ProblemResultRepository problemResultRepository;

  public CompileServiceImp(ProblemRepository problemRepository,
                           ProblemContestResultRepository problemContestResultRepository,
                           ProblemResultRepository problemResultRepository) {
    this.problemRepository = problemRepository;
    this.problemContestResultRepository = problemContestResultRepository;
    this.problemResultRepository = problemResultRepository;
  }

  @Override
  public SubmitProblemOutput submit(SubmitProblemInput input) {
    Problem problem = problemRepository.findById(input.getProblemId()).get();
    Map<String, Object> payloads = new HashMap<>();
    if (input.getSource() != null) {
      payloads.put("source", new FileSystemResource(input.getSource()));
    } else {
      payloads.put("source", new FileSystemResource(FileUtil.writeToFile(input.getSourceString(), "source." + getExtension(input.getLang()))));
    }
    payloads.put("lang", input.getLang());
    payloads.put("id", problem.getCompilerId());
    payloads.put("userId", "2");

    SubmitProblemOutput resCallAPI = (SubmitProblemOutput) ExternalAPICommon.post(CommonConstant.COMPILER_URL, payloads,
        SubmitProblemOutput.class, true);

    if (!resCallAPI.isSuccess()) {
      throw new VsException("Can not compile code");
    }

    int expected = 0;
    for (SubmitProblemItemOutput item : resCallAPI.getData()) {
      if (item.getMessage().equals("Accepted")) {
        expected++;
      }
    }

    ProblemResult problemResult = problemResultRepository.findByProblem_Id(problem.getId());
    if (ObjectUtils.isNotEmpty(problemResult)) {
      problemResult = new ProblemResult();
      problemResult.setTotalPoint(problem.getTotalPoint() * expected / resCallAPI.getData().size());
    } else {
      problemResultRepository.save(new ProblemResult(null,
          problem.getTotalPoint() * expected / resCallAPI.getData().size(), problem));
    }

    return resCallAPI;
  }

  private String getExtension(String lang) {
    //c, c++, C#, java, python, golang, ruby
    if (lang.compareToIgnoreCase("cpp") == 0) {
      return "cpp";
    }
    if (lang.compareToIgnoreCase("c") == 0) {
      return "c";
    }
    if (lang.compareToIgnoreCase("java") == 0) {
      return "java";
    }
    if (lang.compareToIgnoreCase("python") == 0) {
      return "py";
    }
    if (lang.compareToIgnoreCase("C#") == 0) {
      return "cs";
    }
    if (lang.compareToIgnoreCase("golang") == 0) {
      return "go";
    }
    if (lang.compareToIgnoreCase("ruby") == 0) {
      return "ry";
    }

    return "";
  }
}
