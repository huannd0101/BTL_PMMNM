package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV1;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.dto.ContestDto;
import com.hit.compiler.dto.EndContestDto;
import com.hit.compiler.service.ContestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class ContestController {
  private final ContestService contestService;

  public ContestController(ContestService contestService) {
    this.contestService = contestService;
  }

  @GetMapping("/contest")
  public ResponseEntity<?> getAllContest() {
    return VsResponseUtil.ok(contestService.getAllContest());
  }

  @GetMapping("/contest/{id}")
  public ResponseEntity<?> getContestById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(contestService.getContestById(id));
  }

  @PostMapping("/contest")
  public ResponseEntity<?> createContest(@RequestBody ContestDto contestDto) {
    return VsResponseUtil.ok(contestService.createContest(contestDto));
  }

  @PatchMapping("/contest/{id}")
  public ResponseEntity<?> editContestById(@PathVariable("id") Long id, @RequestBody ContestDto contestDto) {
    return VsResponseUtil.ok(contestService.editContest(id, contestDto));
  }

  @DeleteMapping("/contest/{id}")
  public ResponseEntity<?> deleteContestById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(contestService.deleteContest(id));
  }

  @PostMapping("/contest/end")
  public ResponseEntity<?> endContest(@RequestBody EndContestDto dto) {
    return VsResponseUtil.ok(contestService.endContest(dto.getUserId(), dto.getContestId()));
  }

}
