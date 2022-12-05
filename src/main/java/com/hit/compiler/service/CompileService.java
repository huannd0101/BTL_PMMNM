package com.hit.compiler.service;

import com.hit.compiler.input.SubmitProblemInput;
import com.hit.compiler.output.SubmitProblemOutput;

public interface CompileService {

  SubmitProblemOutput submit(SubmitProblemInput input);

}
