package com.sol.wwbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sol.wwbs.model.TaskTree;

public interface TaskTreeRepository extends JpaRepository<TaskTree,Integer> {
	@Query("select t from TaskTree t where t.topLevel is null ")
	List<TaskTree> findRoots(); 
}
