package com.sol.wwbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.wwbs.model.WorkBreakdownStructureTask;

public interface WbsTaskRepository extends JpaRepository<WorkBreakdownStructureTask,String> {

}
