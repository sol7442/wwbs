package com.sol.wwbs.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.wwbs.model.TaskTree;

public interface TaskTreeRepository extends JpaRepository<TaskTree,Integer> {

}
