package com.sol.wwbs.repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sol.wwbs.model.TaskTree;

public interface TaskTreeRepository extends JpaRepository<TaskTree,Integer> {
	@Query("select t from TaskTree t where t.root is null")
	List<TaskTree> findRoots();
	
	@Modifying
    @Query("UPDATE TaskTree t SET t.left = t.left + :range where t.root = :root and t.left > :gapLeft")
    int updateAddLeftGap(@Param("range") int range, @Param("root") TaskTree root, @Param("gapLeft") int gapLeft);
	
	//@Query("select t from wbstree t where t.name = ?1 ")
	@Query("select t from TaskTree t where t.name = ?1 ")
	TaskTree findByName(String name);
}
