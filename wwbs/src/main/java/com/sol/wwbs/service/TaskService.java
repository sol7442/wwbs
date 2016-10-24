package com.sol.wwbs.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.wwbs.model.TaskTree;
import com.sol.wwbs.repository.TaskRepository;
import com.sol.wwbs.repository.TaskTreeRepository;
import com.sol.wwbs.util.tree.TreeActionLocation;
import com.sol.wwbs.util.tree.TreeDao;
import com.sol.wwbs.util.tree.UniqueConstraintViolationException;
import com.sol.wwbs.util.tree.UniqueTreeConstraint;
import com.sol.wwbs.util.tree.TreeActionLocation.ActionType;
import com.sol.wwbs.util.tree.TreeActionLocation.RelatedNodeType;

import fri.util.database.jpa.tree.nestedsets.NestedSetsTreeDao.Location;




@Service
public class TaskService implements TreeDao<TaskTree>{
	@Autowired
	private TaskTreeRepository treeRepo;
	private TaskRepository taskRepo;
	
	private static final int ROOT_LEFT  = 1;
	private static final int ROOT_RIGHT = 2;
	@Override
	public boolean isPersistent(TaskTree entity) {
		return entity.getId() == null;
	}
	@Override
	public TaskTree find(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(TaskTree entity) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isRoot(TaskTree entity) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public TaskTree createRoot(TaskTree root) throws UniqueConstraintViolationException {
		root.setLeft(ROOT_LEFT);
		root.setRight(ROOT_RIGHT);
		
		return treeRepo.save(root);
	}
	@Override
	public int size(TaskTree tree) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<TaskTree> getRoots() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<TaskTree> getTree(TaskTree parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TaskTree> getTreeCacheable(TaskTree parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TaskTree> findSubTree(TaskTree parent, List<TaskTree> treeCacheable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TaskTree> findDirectChildren(List<TaskTree> treeCacheable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isLeaf(TaskTree node) {
		return node.getLeft() + 1 == node.getRight();
	}
	@Override
	public int getChildCount(TaskTree parent) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<TaskTree> getChildren(TaskTree parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskTree getRoot(TaskTree node) {
		// TODO Auto-generated method stub
		return null;
	}
	public TaskTree getRoot(String name) {
		return treeRepo.findByName(name);
	}
	
	@Override
	public TaskTree getParent(TaskTree node) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TaskTree> getPath(TaskTree node) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getLevel(TaskTree node) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isEqualToOrChildOf(TaskTree child, TaskTree parent) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildOf(TaskTree child, TaskTree parent) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public TaskTree addChild(TaskTree parent, TaskTree child) throws UniqueConstraintViolationException {
		return addChildAt(parent, child, UNDEFINED_POSITION);
	}
	@Override
	public TaskTree addChildAt(TaskTree parent, TaskTree child, int position)
			throws UniqueConstraintViolationException {
		Location location = location(parent, position, null, false);
		
		return addChild(location,child);
	}
	private TaskTree addChild(Location location, TaskTree child) {
		if(isPersistent(child)){
			throw new IllegalArgumentException("Node is already persistent, can not be added as child: "+child);
		}
		final TaskTree root = location.root;
		child.setRoot(root);
		child.setLeft(location.targetLeft);
		child.setRight(location.targetRight);
		
		//checkUniqueness
		createGap(location.targetLeft, root, 1);
		
		return null;
	}
	private void createGap(int gapLeft, TaskTree root, int nodesCount) {
		
		treeRepo.updateAddLeftGap(nodesCount*2, root,gapLeft);
		
	//	gap("+", gapLeft, gapLeft, root, nodesCount);
	}
//	private void gap(String string, int gapLeft, int gapLeft2, TaskTree root, int nodesCount) {
//		// TODO Auto-generated method stub
//		
//	}
	@Override
	public TaskTree addChildBefore(TaskTree sibling, TaskTree child) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void remove(TaskTree node) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void move(TaskTree node, TaskTree newParent) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveTo(TaskTree node, TaskTree parent, int position) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveBefore(TaskTree node, TaskTree sibling) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveToBeRoot(TaskTree child) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public TaskTree copy(TaskTree node, TaskTree parent, TaskTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskTree copyTo(TaskTree node, TaskTree parent, int position, TaskTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskTree copyBefore(TaskTree node, TaskTree sibling, TaskTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskTree copyToBeRoot(TaskTree child, TaskTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setCopiedNodeRenamer(com.sol.wwbs.util.tree.TreeDao.CopiedNodeRenamer<TaskTree> copiedNodeRenamer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<TaskTree> find(TaskTree parent, Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setUniqueTreeConstraint(UniqueTreeConstraint<TaskTree> uniqueTreeConstraint) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setCheckUniqueConstraintOnUpdate(boolean checkUniqueConstraintOnUpdate) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void checkUniqueConstraint(TaskTree cloneOfExistingNodeWithNewValues, TaskTree root, TaskTree originalNode)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	
//	public TaskTree createTaskTree(TaskTree tree){
//		return treeRepo.save(tree);
//	}
////	public TaskTree createRoot(TaskTree root){
////				
////		root.setLeft(ROOT_LEFT);
////		root.setRight(ROOT_RIGHT);
////		
////		//Location location = new Location(null, TreeActionLocation.RelatedNodeType.PARENT, null, TreeActionLocation.ActionType.INSERT, ROOT_LEFT);
////		//checkUniqueness(Arrays.asList(new NestedSetsTreeNode [] { root }), location);
////		
////		return treeRepo.save(root);
////		
////	}
//	
//	public TaskTree addTask(TaskTree root, TaskTree tree){
//		Location location = null;//new Location(root, relatedNodeType, relatedNode, null, targetLeft);
//		return addTask(location, tree);
//	}
//	
//	private TaskTree addTask(Location location, TaskTree tree){
//		
//		
//		
//		return null;
//	}
//	
	private Location location(TaskTree parent, int position, TaskTree moveNode, ActionType type){
		
		if(isLeaf(parent)){
			return new Location(parent.getRoot(), RelatedNodeType.PARENT, parent, type, parent.getLeft() + 1);
		}
		
//		if (position <= UNDEFINED_POSITION){
//			return new Location(parent.getTopLevel(), TreeActionLocation.RelatedNodeType.PARENT, parent, actionType, parent.getRight());
//		}
		
		
		return location;
	}
	
	private static class Location extends TreeActionLocation<TaskTree>{
		public final int targetLeft;
		public final int targetRight;
		public Location(TaskTree root, RelatedNodeType relatedNodeType, TaskTree relatedNode, ActionType actionType, int targetLeft) {
			super(root, relatedNodeType, relatedNode, actionType);
			this.targetLeft  = targetLeft;
			this.targetRight = targetLeft + 1; 
		}
	}
}
