package com.sol.wwbs.repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sol.wwbs.model.TaskTree;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;

public interface TaskTreeRepository extends JpaRepository<TaskTree,Integer> {
	@Query("select t from TaskTree t where t.root is null")
	List<TaskTree> findRoots();
	
	@Transactional
	@Modifying //update _tree t set t.lft = t.lft + 2 where t.root = 1 and t.lft > 2
    @Query("update TaskTree t set t.left = t.left + ?2 where t.root = ?1 and t.left > ?3")
    void updateAddLeftGap(TaskTree root,int range, int left);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update TaskTree t set t.right = t.right + ?2 where t.root = ?1 and t.right >= ?3")
    void updateAddRightGap(TaskTree root,int range, int right);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update TaskTree t set t.left = t.left - ?2 where t.root = ?1 and t.left > ?3")
	void updateDelLeftGap(TaskTree root, int range, int left);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update TaskTree t set t.right = t.right - ?2 where t.root = ?1 and t.right > ?3")
    void updateDelRightGap(TaskTree root,int range, int right);
	
	
	//@Query("select t from wbstree t where t.name = ?1 ")
	@Query("select t from TaskTree t where t.name = ?1 ")
	TaskTree findByName(String name);

	@Query("select t from TaskTree t where t.root = ?1 and t.left > ?2 and t.right < ?3 order by t.left ")
	List<TaskTree> getChildren(TaskTree root,int left, int right );

	//TODO === remove
	
	@Transactional
	@Modifying
	@Query("update TaskTree t set t.root = null where t.root = ?1")
	void updateRootNull(TaskTree node);
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("update TaskTree t set t.root = null where t.root = ?1 and t.left >= ?2 and t.right <= ?3")
	void updateRootNull(TaskTree node, int left, int right);
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("delete from TaskTree t where (t.root is null or t.root = ?1) and t.left >= ?2 and t.right <= ?3")
	void delete(TaskTree node,int left, int right);

	

	
}
