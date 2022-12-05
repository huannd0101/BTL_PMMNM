package com.hit.compiler.service;

import com.hit.compiler.dto.EditProblem;
import com.hit.compiler.dto.ProblemDto;
import com.hit.compiler.entity.Problem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ProblemService {
  List<Problem> getAllProblem();

  Problem getProblemById(Long id);

  Problem createProblem(ProblemDto problemDto) throws IOException;

  Problem editProblem(Long id, EditProblem editProblem);

  Problem deleteProblem(Long id);
}
