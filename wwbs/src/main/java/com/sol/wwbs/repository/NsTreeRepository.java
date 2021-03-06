package com.sol.wwbs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sol.wwbs.model.NsTree;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;

public interface NsTreeRepository extends JpaRepository<NsTree,Integer> {
	@Query("select t from #{#entityName} t where t.root is null")
	List<NsTree> findRoots();
	
	@Transactional
	@Modifying //update _tree t set t.lft = t.lft + 2 where t.root = 1 and t.lft > 2
    @Query("update #{#entityName} t set t.left = t.left + ?2 where t.root = ?1 and t.left >= ?3")
    void updateAddLeftGap(NsTree root,int range, int left);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update #{#entityName} t set t.right = t.right + ?2 where t.root = ?1 and t.right >= ?3")
    void updateAddRightGap(NsTree root,int range, int right);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update #{#entityName} t set t.left = t.left - ?2 where t.root = ?1 and t.left >= ?3")
	void updateDelLeftGap(NsTree root, int range, int left);
	
	@Transactional
	@Modifying //update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3
    @Query("update #{#entityName} t set t.right = t.right - ?2 where t.root = ?1 and t.right >= ?3")
    void updateDelRightGap(NsTree root,int range, int right);
	
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("update #{#entityName} t set t.left = t.left + ?2 where t.root = ?1 and t.left >= ?3 and t.left <= ?4")
	void updateRangeLeft(NsTree root, int range, int left, int right);

	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("update #{#entityName} t set t.right = t.right + ?2 where t.root = ?1 and t.right >= ?3 and t.right <= ?4")
	void updateRangeRight(NsTree root, int range, int left, int right);
	
	
//	//@Query("select t from wbstree t where t.name = ?1 ")
//	@Query("select t from #{#entityName} t where t.name = ?1 ")
//	NsTree findByName(String name);

	@Query("select t from #{#entityName} t where t.root = ?1 and t.left > ?2 and t.right < ?3 order by t.left ")
	List<NsTree> findSubTree(NsTree root,int left, int right );

	//TODO === remove
	
	@Transactional
	@Modifying
	@Query("update #{#entityName} t set t.root = null where t.root = ?1")
	void updateRootNull(NsTree root);
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("update #{#entityName} t set t.root = null where t.root = ?1 and t.left >= ?2 and t.right <= ?3")
	void updateRootNull(NsTree root, int left, int right);
	
	@Transactional
	@Modifying //delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.lft <=15
	@Query("delete from #{#entityName} t where (t.root is null or t.root = ?1) and t.left >= ?2 and t.right <= ?3")
	void delete(NsTree root,int left, int right);
	
	

//	@Query("select t from #{#entityName} t where t.name = ?1")
//	List<NsTree> findByTreeName(String name);
	
	@Query("select t from #{#entityName} t where t.root = t")
	List<NsTree> getRoots();
	
	@Query("select t from #{#entityName} t where t.root = ?1 and t.left >=?2 and t.right <=?3 and t.depth =?4")
	List<NsTree> findChildren(NsTree root, int left, int right, int depth);
	
	@Query("select t from #{#entityName} t where t.root = ?1 and t.left <?2 and t.right > ?3 and t.depth =?4")
	NsTree findParent(NestedSetsTreeNode root, int left, int right, int depth);

	@Query("select t from #{#entityName} t where t.root = ?1 and t.left <=?2 and t.right >= ?3 order by t.left")
	List<NsTree> getPath(NestedSetsTreeNode root, int left, int right);

	@Query("select t from #{#entityName} t where t.root = ?1 order by t.left")
	List<NsTree> findAll(NestedSetsTreeNode root);

	

	
}
