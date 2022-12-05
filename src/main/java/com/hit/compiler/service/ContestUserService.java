package com.hit.compiler.service;

import com.hit.compiler.dto.ContestUserDto;
import com.hit.compiler.entity.ContestUser;

public interface ContestUserService {

  ContestUser createNewContestUser(ContestUserDto contestUserDto);

}
