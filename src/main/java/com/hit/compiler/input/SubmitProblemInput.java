package com.hit.compiler.input;

import com.hit.compiler.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
public class SubmitProblemInput {

  private File source;

  private String lang;

  private Long problemId;

  private String sourceString;

  public SubmitProblemInput(MultipartFile source, String lang, Long problemId, String sourceString) {
    if (source != null) {
      this.source = FileUtil.convertMultipartToFile(source);
    }
    this.lang = lang;
    this.problemId = problemId;
    this.sourceString = sourceString;
  }
}
