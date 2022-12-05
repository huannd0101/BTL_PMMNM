package com.hit.compiler.controller;

import com.hit.compiler.base.RestApiV1;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.input.SubmitProblemInput;
import com.hit.compiler.service.CompileService;
import com.hit.compiler.util.FileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
public class CompileController {
  private final CompileService compileService;

  public CompileController(CompileService compileService) {
    this.compileService = compileService;
  }

  @PostMapping("/compiler/submit")
  public ResponseEntity<?> submit(@RequestPart(value = "source", required = false) MultipartFile file,
                                  @RequestParam("lang") String lang,
                                  @RequestParam("problemId") Long problemId,
                                  @RequestParam(value = "sourceString", required = false) String sourceString) {
    SubmitProblemInput input = new SubmitProblemInput(file, lang, problemId, sourceString);

    return VsResponseUtil.ok(compileService.submit(input));
  }

}
