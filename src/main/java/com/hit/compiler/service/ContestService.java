package com.hit.compiler.service;

import com.hit.compiler.dto.ContestDto;
import com.hit.compiler.entity.Contest;
import com.hit.compiler.entity.ContestUser;
import com.hit.compiler.payload.TrueFalseResponse;

import java.util.List;

public interface ContestService {
  List<Contest> getAllContest();

  Contest createContest(ContestDto contestDto);

  Contest editContest(Long id, ContestDto contestDto);

  Contest deleteContest(Long id);

  Contest getContestById(Long id);

  ContestUser endContest(Long userId, Long contestId);
}
