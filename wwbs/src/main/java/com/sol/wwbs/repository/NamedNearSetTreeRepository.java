package com.sol.wwbs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.wwbs.model.NamedNearSetTree;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;

public interface NamedNearSetTreeRepository extends JpaRepository<NamedNearSetTree,Integer> {
	@Query("select t from NamedNearSetTree t where t.root is null")
	List<NamedNearSetTree> findRoots();
	
	@Transactional
	@Modifying //update _tree t set t.lft = t.lft + 2 where t.root = 1 and t.lft > 2
    @Query("update NamedNearSetTree t set t.left = t.left + ?2 where t.root = ?1 and t.left > ?3")
    void updateAddLeftGap(NamedNearSetTree root,int range, int left);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update NamedNearSetTree t set t.right = t.right + ?2 where t.root = ?1 and t.right >= ?3")
    void updateAddRightGap(NamedNearSetTree root,int range, int right);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update NamedNearSetTree t set t.left = t.left - ?2 where t.root = ?1 and t.left > ?3")
	void updateDelLeftGap(NamedNearSetTree root, int range, int left);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update NamedNearSetTree t set t.right = t.right - ?2 where t.root = ?1 and t.right > ?3")
    void updateDelRightGap(NamedNearSetTree root,int range, int right);
	
	
	//@Query("select t from wbstree t where t.name = ?1 ")
	@Query("select t from NamedNearSetTree t where t.name = ?1 ")
	NamedNearSetTree findByName(String name);

	@Query("select t from NamedNearSetTree t where t.root = ?1 and t.left > ?2 and t.right < ?3 order by t.left ")
	List<NamedNearSetTree> getChildren(NamedNearSetTree root,int left, int right );

	//TODO === remove
	
	@Transactional
	@Modifying
	@Query("update NamedNearSetTree t set t.root = null where t.root = ?1")
	void updateRootNull(NamedNearSetTree root);
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("update NamedNearSetTree t set t.root = null where t.root = ?1 and t.left >= ?2 and t.right <= ?3")
	void updateRootNull(NamedNearSetTree root, int left, int right);
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("delete from NamedNearSetTree t where (t.root is null or t.root = ?1) and t.left >= ?2 and t.right <= ?3")
	void delete(NamedNearSetTree root,int left, int right);
//
//	@Transactional
//	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
//	@Query("update NamedNearSetTree t set t.name = ?1 where t.root = 1 and t.left >= ?2 and t.right <= ?3")
//	void rename(String name, int left, int right);
//
//	@Transactional
//	@Modifying 
//	@Query("delete from NamedNearSetTree t where t.name= ?1")
//	void deleteByName(String name);
//	
//	@Transactional
//	@Modifying 
//	@Query("delete from NamedNearSetTree t where t.left >= ?1 and t.right <= ?2")
//	void deleteByRange(int left, int right);

	@Query("select t from NamedNearSetTree t where t.name = ?1")
	List<NamedNearSetTree> findByTreeName(String name);

	

	
}