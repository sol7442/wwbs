package com.sol.wwbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.wwbs.model.Task;

public interface TaskRepository extends JpaRepository<Task,String> {

}
