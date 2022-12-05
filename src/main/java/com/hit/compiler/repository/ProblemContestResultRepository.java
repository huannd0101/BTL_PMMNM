package com.hit.compiler.repository;

import com.hit.compiler.entity.ProblemContestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemContestResultRepository extends JpaRepository<ProblemContestResult, Long> {
}
