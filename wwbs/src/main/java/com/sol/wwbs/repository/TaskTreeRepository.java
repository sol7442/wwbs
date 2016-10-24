package com.sol.wwbs.repository;


import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sol.wwbs.model.TaskTree;

public interface TaskTreeRepository extends JpaRepository<TaskTree,Integer> {
	@Query("select t from TaskTree t where t.root is null")
	List<TaskTree> findRoots();
	
//	@Modifying
//    @Query("UPDATE _tree t SET t.lft = t.lft + :range where t.root = :root and t.lft > :gapLeft")
//    int updateAddLeftGap(@Param("range") int range, @Param("root") TaskTree root, @Param("gapLeft") int gapLeft);
	
	//@Query("select t from wbstree t where t.name = ?1 ")
	@Query("select t from TaskTree t where t.name = ?1 ")
	TaskTree findByName(String name);
}
