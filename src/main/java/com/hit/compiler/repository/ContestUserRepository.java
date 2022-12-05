package com.hit.compiler.repository;

import com.hit.compiler.entity.ContestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestUserRepository extends JpaRepository<ContestUser, Long> {

  @Query("select c from ContestUser c where c.contest.id = ?1 and c.user.id = ?2")
  ContestUser findByContest_IdAndUser_Id(Long contestId, Long userId);

}
