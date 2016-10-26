package com.sol.wwbs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.wwbs.model.NamedNearSetTree;
import com.sol.wwbs.repository.TaskRepository;
import com.sol.wwbs.repository.NamedNearSetTreeRepository;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;
import com.sol.wwbs.util.tree.TreeActionLocation;
import com.sol.wwbs.util.tree.TreeDao;
import com.sol.wwbs.util.tree.UniqueConstraintViolationException;
import com.sol.wwbs.util.tree.UniqueTreeConstraint;
import com.sol.wwbs.util.tree.TreeActionLocation.ActionType;
import com.sol.wwbs.util.tree.TreeActionLocation.RelatedNodeType;






@Service
public class TaskService implements TreeDao<NamedNearSetTree>{
	@Autowired
	private NamedNearSetTreeRepository treeRepo;
	private TaskRepository taskRepo;
	
	private static final int ROOT_LEFT  = 1;
	private static final int ROOT_RIGHT = 2;
//	@Override
//	public boolean isPersistent(TaskTree entity) {
//		return false;//entity.getId() == null;
//	}
	@Override
	public NamedNearSetTree find(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(NamedNearSetTree entity) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isRoot(NamedNearSetTree entity) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public NamedNearSetTree createRoot(NamedNearSetTree root) throws UniqueConstraintViolationException {
		root.setLeft(ROOT_LEFT);
		root.setRight(ROOT_RIGHT);
		
		return treeRepo.save(root);
	}
	@Override
	public int size(NamedNearSetTree tree) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<NamedNearSetTree> getRoots() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<NamedNearSetTree> getTree(NamedNearSetTree parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<NamedNearSetTree> getTreeCacheable(NamedNearSetTree parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<NamedNearSetTree> findSubTree(NamedNearSetTree parent, List<NamedNearSetTree> treeCacheable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<NamedNearSetTree> findDirectChildren(List<NamedNearSetTree> subNodes) {
//		final int size = subNodes.size();
//		if (size <= 1){	
//			return Collections.unmodifiableList(children);
//		}
//		
//		NestedSetsTreeNode parent = subNodes.get(0);
//		subNodes = subNodes.subList(1, size);
//		
//		int nextChildLeft = parent.getLeft() + 1;	// this is 'left' of first child
//		int currentChildRight = subNodes.get(0).getRight();
//		int i = 0;
//		for (NestedSetsTreeNode node : subNodes)	{
//			if (isNextChild(nextChildLeft, node, currentChildRight, subNodes, i))	{
//				if (isValidFilterChild(node))	{
//					children.add(node);
//					currentChildRight = node.getRight();
//				}
//				nextChildLeft = node.getRight() + 1;	// calculate 'left' of next child
//			}
//			i++;
//		}
		
		return null;
	}
//	@Override
//	public boolean isLeaf(NamedNearSetTree node) {
//		return node.getLeft() + 1 == node.getRight();
//	}
	@Override
	public int getChildCount(NamedNearSetTree parent) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<NamedNearSetTree> getChildren(NamedNearSetTree parent) {
		return treeRepo.getChildren((NamedNearSetTree)parent.getRoot(),parent.getLeft(),parent.getRight());
		
		
//		List<TaskTree> subTree = getSubTreeDepthFirst(parent);
//		return findDirectChildren(subTree);
	}
	private List<NamedNearSetTree> getSubTreeDepthFirst(NamedNearSetTree parent) {
		//select * from _tree t where t.root = 1 and t.lft >=1 and t.rgt <=2 order by t.lft;
		return null;
	}
	@Override
	public NamedNearSetTree getRoot(NamedNearSetTree node) {
		// TODO Auto-generated method stub
		return null;
	}
	public NamedNearSetTree getRoot(String name) {
		return treeRepo.findByName(name);
	}
	
	@Override
	public NamedNearSetTree getParent(NamedNearSetTree node) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<NamedNearSetTree> getPath(NamedNearSetTree node) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getLevel(NamedNearSetTree node) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isEqualToOrChildOf(NamedNearSetTree child, NamedNearSetTree parent) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildOf(NamedNearSetTree child, NamedNearSetTree parent) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public NamedNearSetTree addChild(NamedNearSetTree parent, NamedNearSetTree child) throws UniqueConstraintViolationException {
		return addChildAt(parent, child, UNDEFINED_POSITION);
	}
	@Override
	public NamedNearSetTree addChildAt(NamedNearSetTree parent, NamedNearSetTree child, int position)
			throws UniqueConstraintViolationException {
		Location location = location(parent, position, null, ActionType.INSERT);
		
		return addChild(parent,child,location);
	}
	private NamedNearSetTree addChild(NamedNearSetTree parent, NamedNearSetTree child,Location location) {
		if(child.isPersistent()){
			throw new IllegalArgumentException("Node is already persistent, can not be added as child: "+child);
		}
		
		final NamedNearSetTree root = (NamedNearSetTree)location.root;
		child.setRoot(root);
		child.setDepth(parent.getDepth() + 1);
		child.setLeft(location.targetLeft);
		child.setRight(location.targetRight);
		
		//checkUniqueness
		treeRepo.updateAddLeftGap(root,2,location.targetLeft);
		treeRepo.updateAddRightGap(root,2, location.targetLeft);
		
		treeRepo.flush();
		
		return treeRepo.save(child);
	}
//	private void createGap(int gapLeft, TaskTree root, int nodesCount) {
//		
//		treeRepo.updateAddLeftGap(nodesCount*2, root,gapLeft);
//		
//	//	gap("+", gapLeft, gapLeft, root, nodesCount);
//	}
//	private void gap(String string, int gapLeft, int gapLeft2, TaskTree root, int nodesCount) {
//		// TODO Auto-generated method stub
//		
//	}
	@Override
	public NamedNearSetTree addChildBefore(NamedNearSetTree sibling, NamedNearSetTree child) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void remove(NamedNearSetTree node) {
		if(node == null || !node.isPersistent()){
			throw new IllegalArgumentException("Node is null or not persistent: "+node);
		}

		final NamedNearSetTree root = (NamedNearSetTree)node.getRoot();
		
		if(node.isRoot()){
			treeRepo.updateRootNull(root, node.getLeft(),node.getRight());
		}
		
		System.out.println(node.toString() + ":" +node.getLeft() + "," + node.getRight());
		
		treeRepo.delete(root,node.getLeft(),node.getRight());
		
		treeRepo.updateDelLeftGap(root,node.numberOfNodesInSubTree()*2,node.getLeft());
		treeRepo.updateDelRightGap(root,node.numberOfNodesInSubTree()*2,node.getRight());
		
		//treeRepo.delete(node);
		
		//treeRepo.flush();
	}
	

//	public void rename(TaskTree node) {
//		final TaskTree root = (TaskTree)node.getRoot();
//		System.out.println(node.toString() + ":" +node.getLeft() + "," + node.getRight());
//		
//		treeRepo.rename(node.getName(),node.getLeft(),node.getRight());
//		
////		treeRepo.updateDelLeftGap(root,node.numberOfNodesInSubTree()*2,node.getLeft());
////		treeRepo.updateDelRightGap(root,node.numberOfNodesInSubTree()*2,node.getRight());
//		
//	}
	
	@Override
	public void move(NamedNearSetTree node, NamedNearSetTree newParent) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveTo(NamedNearSetTree node, NamedNearSetTree parent, int position) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveBefore(NamedNearSetTree node, NamedNearSetTree sibling) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveToBeRoot(NamedNearSetTree child) throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public NamedNearSetTree copy(NamedNearSetTree node, NamedNearSetTree parent, NamedNearSetTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NamedNearSetTree copyTo(NamedNearSetTree node, NamedNearSetTree parent, int position, NamedNearSetTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NamedNearSetTree copyBefore(NamedNearSetTree node, NamedNearSetTree sibling, NamedNearSetTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NamedNearSetTree copyToBeRoot(NamedNearSetTree child, NamedNearSetTree copiedNodeTemplate)
			throws UniqueConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setCopiedNodeRenamer(com.sol.wwbs.util.tree.TreeDao.CopiedNodeRenamer<NamedNearSetTree> copiedNodeRenamer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<NamedNearSetTree> find(NamedNearSetTree parent, Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setUniqueTreeConstraint(UniqueTreeConstraint<NamedNearSetTree> uniqueTreeConstraint) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setCheckUniqueConstraintOnUpdate(boolean checkUniqueConstraintOnUpdate) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void checkUniqueConstraint(NamedNearSetTree cloneOfExistingNodeWithNewValues, NamedNearSetTree root, NamedNearSetTree originalNode)
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
	private Location location(NamedNearSetTree parent, int position, NamedNearSetTree moveNode, ActionType actionType){
		
		if(parent.isLeaf()){
			return new Location(parent.getRoot(), RelatedNodeType.PARENT, parent, actionType, parent.getLeft() + 1);
		}
		
		if (position <= UNDEFINED_POSITION){
			return new Location(parent.getRoot(), TreeActionLocation.RelatedNodeType.PARENT, parent, actionType, parent.getRight());
		}
		
		 
		List<NamedNearSetTree> children = getChildListForInsertion(parent);
		if (position >= children.size()){
			return new Location(parent.getRoot(), TreeActionLocation.RelatedNodeType.PARENT, parent, actionType, parent.getRight());
		}
		
		NamedNearSetTree sibling = getSiblingNode(children,moveNode,position);
		return new Location(parent, RelatedNodeType.SIBLING, sibling, actionType, sibling.getLeft());
	}
	
	private List<NamedNearSetTree> getChildListForInsertion(NamedNearSetTree parent) {
		return getChildren(parent);
	}
	private NamedNearSetTree getSiblingNode(List<NamedNearSetTree> children,NamedNearSetTree moveNode, int position) {
		NamedNearSetTree sibling = null;
		if (moveNode != null)	{
			int movingChildPosition = children.indexOf(moveNode);
			if (movingChildPosition >= 0 && movingChildPosition < position)	{
				if (position + 1 < children.size())	{
					sibling = children.get(position + 1);
				}
			}
		}
		return sibling;
	}

	private static class Location extends TreeActionLocation<NestedSetsTreeNode>{
		public final int targetLeft;
		public final int targetRight;
		public Location(NestedSetsTreeNode root, RelatedNodeType relatedNodeType, NestedSetsTreeNode relatedNode, ActionType actionType, int targetLeft) {
			super(root, relatedNodeType, relatedNode, actionType);
			this.targetLeft  = targetLeft;
			this.targetRight = targetLeft + 1; 
		}
	}

	public List<NamedNearSetTree> getTree(String name) {
		return treeRepo.findByTreeName(name);
	}

//	public void removeByName(String name) {
//		treeRepo.deleteByName(name);
//	}
//	public void removeByRange(int left, int right) {
//		treeRepo.deleteByRange(left, right);
//	}

}
