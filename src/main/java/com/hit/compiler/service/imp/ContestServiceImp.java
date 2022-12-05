package com.hit.compiler.service.imp;

import com.hit.compiler.dto.ContestDto;
import com.hit.compiler.entity.Contest;
import com.hit.compiler.entity.ContestUser;
import com.hit.compiler.entity.Problem;
import com.hit.compiler.entity.ProblemResult;
import com.hit.compiler.exception.VsException;
import com.hit.compiler.repository.ContestRepository;
import com.hit.compiler.repository.ContestUserRepository;
import com.hit.compiler.repository.ProblemResultRepository;
import com.hit.compiler.service.ContestService;
import com.hit.compiler.util.DataUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContestServiceImp implements ContestService {
  private final ContestRepository contestRepository;
  private final ContestUserRepository contestUserRepository;
  private final ProblemResultRepository problemResultRepository;

  public ContestServiceImp(ContestRepository contestRepository, ContestUserRepository contestUserRepository,
                           ProblemResultRepository problemResultRepository) {
    this.contestRepository = contestRepository;
    this.contestUserRepository = contestUserRepository;
    this.problemResultRepository = problemResultRepository;
  }

  @Override
  public List<Contest> getAllContest() {
    return contestRepository.findAll();
  }

  @Override
  public Contest createContest(ContestDto contestDto) {
    if (contestRepository.findByName(contestDto.getName()) != null) {
      throw new VsException("Duplicate name " + contestDto.getName());
    }
    Contest contest = new Contest(contestDto.getName(), contestDto.getDescription(), contestDto.getStartTime(),
        contestDto.getEndTime(), contestDto.getPassword(), contestDto.getStatus());
    return contestRepository.save(contest);
  }

  @Override
  public Contest editContest(Long id, ContestDto contestDto) {
    Contest contest = getContestById(id);
    contest.setDescription(contestDto.getDescription());
    contest.setName(contestDto.getName());
    contest.setStatus(contestDto.getStatus());
    contest.setEndTime(contestDto.getEndTime());
    contest.setStartTime(contestDto.getStartTime());
    contest.setPassword(DataUtil.getPassword(contestDto.getPassword()));
    return contestRepository.save(contest);
  }

  @Override
  public Contest deleteContest(Long id) {
    Contest contest = getContestById(id);
    contestRepository.delete(contest);
    return contest;
  }

  @Override
  public Contest getContestById(Long id) {
    Optional<Contest> contest = contestRepository.findById(id);
    if (contest.isEmpty()) {
      throw new VsException("Can not find contest by id = " + id);
    }
    return contest.get();
  }

  @Override
  public ContestUser endContest(Long userId, Long contestId) {
    ContestUser contestUser = contestUserRepository.findByContest_IdAndUser_Id(contestId, userId);
    if (contestUser == null) {
      throw new VsException("Can not find contestId = " + contestId + " and userId = " + userId);
    }
    Contest contest = contestRepository.findById(contestUser.getContest().getId()).get();

    long total = 0;
    for (Problem problem : contest.getProblems()) {
      ProblemResult problemResult = problemResultRepository.findByProblem_Id(problem.getId());
      if (ObjectUtils.isNotEmpty(problemResult)) {
        total += problemResult.getTotalPoint();
      }
    }

    contestUser.setTotalPoint(total);

    return contestUserRepository.save(contestUser);
  }

}
