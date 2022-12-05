package com.hit.compiler.repository;

import com.hit.compiler.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
  Contest findByName(String name);
}
