package com.hit.compiler.repository;

import com.hit.compiler.entity.ProblemResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemResultRepository extends JpaRepository<ProblemResult, Long> {

  @Query("select p from ProblemResult p where p.problem.id = ?1")
  ProblemResult findByProblem_Id(Long id);

}
