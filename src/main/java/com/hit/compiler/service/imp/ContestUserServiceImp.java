package com.hit.compiler.service.imp;

import com.hit.compiler.dto.ContestUserDto;
import com.hit.compiler.entity.ContestUser;
import com.hit.compiler.repository.ContestRepository;
import com.hit.compiler.repository.ContestUserRepository;
import com.hit.compiler.repository.UserRepository;
import com.hit.compiler.service.ContestUserService;
import org.springframework.stereotype.Service;

@Service
public class ContestUserServiceImp implements ContestUserService {
  private final ContestUserRepository contestUserRepository;
  private final UserRepository userRepository;
  private final ContestRepository contestRepository;

  public ContestUserServiceImp(ContestUserRepository contestUserRepository, UserRepository userRepository,
                               ContestRepository contestRepository) {
    this.contestUserRepository = contestUserRepository;
    this.userRepository = userRepository;
    this.contestRepository = contestRepository;
  }

  @Override
  public ContestUser createNewContestUser(ContestUserDto contestUserDto) {
    ContestUser contestUser = new ContestUser();
    contestUser.setUser(userRepository.findById(contestUserDto.getUserId()).get());
    contestUser.setContest(contestRepository.findById(contestUserDto.getContestId()).get());
    return contestUserRepository.save(contestUser);
  }

}
