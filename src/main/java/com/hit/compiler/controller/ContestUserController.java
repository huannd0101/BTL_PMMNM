package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV1;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.dto.ContestUserDto;
import com.hit.compiler.service.ContestUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
public class ContestUserController {
  private final ContestUserService contestUserService;

  public ContestUserController(ContestUserService contestUserService) {
    this.contestUserService = contestUserService;
  }

  @PostMapping("/contest-users")
  public ResponseEntity<?> createNewContestUser(@RequestBody ContestUserDto contestUserDto) {
    return VsResponseUtil.ok(contestUserService.createNewContestUser(contestUserDto));
  }

}
