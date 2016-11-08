package com.sol.wwbs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.wwbs.model.NsTree;
import com.sol.wwbs.repository.NsTreeRepository;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;
import com.sol.wwbs.util.tree.TreeActionLocation;
import com.sol.wwbs.util.tree.TreeDao;
import com.sol.wwbs.util.tree.TreeActionLocation.ActionType;
import com.sol.wwbs.util.tree.TreeActionLocation.RelatedNodeType;



@Service
public class NsTreeService implements TreeDao<NsTree>{
	
	@Autowired
	private NsTreeRepository treeRepo;
	
	private static final int ROOT_LEFT  = 1;
	private static final int ROOT_RIGHT = 2;

	
	@Override
	public NsTree find(int id) {
		return treeRepo.findOne(id);
	}
//	public List<NsTree> find(String name) {
//		return treeRepo.findByTreeName(name);
//	}
	
	public List<NsTree> findAll(NsTree root) {
		return treeRepo.findAll(root);
	}
	
	@Override
	public NsTree update(NsTree node)  {
		return treeRepo.save(node);
	}
	@Override
	public NsTree createRoot(NsTree root)  {
		root.setLeft(ROOT_LEFT);
		root.setRight(ROOT_RIGHT);
		return treeRepo.save(root);
	}
	
	@Override
	public List<NsTree> findRoots() {
		return treeRepo.getRoots();
	}
//	public NsTree findRoot(String name) {
//		return treeRepo.findByName(name);
//	}
	
	
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<NsTree> findSubTree(NsTree parent) {
		if(parent == null){
			return new ArrayList<>();
		}
		return treeRepo.findSubTree((NsTree)parent.getRoot(),parent.getLeft(),parent.getRight());
	}
	@Override
	public List<NsTree> findChildren(NsTree parent) {
		if(parent == null){
			return new ArrayList<>();
		}
		return treeRepo.findChildren((NsTree)parent.getRoot(),parent.getLeft(),parent.getRight(),parent.getDepth() + 1);
	}
	
	@Override
	public NsTree findParent(NsTree node) {
		if(node.isRoot()) return null;
		return treeRepo.findParent(node.getRoot(),node.getLeft(),node.getRight(),node.getDepth() - 1);
	}
	@Override
	public List<NsTree> getPath(NsTree node) {
		return treeRepo.getPath(node.getRoot(),node.getLeft(),node.getRight());
	}
	
	@Override
	public NsTree addChild(NsTree parent, NsTree child)  {
		return addChildAt(parent, child, UNDEFINED_POSITION);
	}
	
	@Override
	public NsTree addChildAt(NsTree parent, NsTree child, int position)
			 {
		Location location = location(parent, position, null, ActionType.INSERT);
		
		return addChild(child,location,parent.getDepth() + 1);
	}
	
	@Override
	public NsTree addChildBefore(NsTree sibling, NsTree child)  {
		Location location = new Location(sibling.getRoot(), RelatedNodeType.SIBLING, sibling, ActionType.INSERT, sibling.getLeft());
		return addChild(child,location,sibling.getDepth());
	}
	
	private NsTree addChild(NsTree child,Location location,int depth) {
		if(child.isPersistent()){
			throw new IllegalArgumentException("Node is already persistent, can not be added as child: "+child);
		}
		
		final NsTree root = (NsTree)location.root;
		
		child.setRoot(root);
		changeDepth(child, depth);
		child.setLeft(location.targetLeft);
		child.setRight(location.targetLeft + 1);
		
		//checkUniqueness
		treeRepo.updateAddLeftGap(root,2,location.targetLeft);
		treeRepo.updateAddRightGap(root,2, location.targetLeft);
		
		treeRepo.flush();
		
		return treeRepo.save(child);
	}
	public void changeDepth(NsTree node,int depth){
		List<NsTree> children =  findChildren(node);
		for(NsTree child : children){
			changeDepth(child, depth + 1);
		}
		node.setDepth(depth);
		
		if(!node.isPersistent()){
			return;
		}
		treeRepo.save(node);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void remove(NsTree node) {
		if(node == null ){
			throw new IllegalArgumentException("Node is null  "+node);
		}
		
		if(!node.isPersistent()){
			throw new IllegalArgumentException("Node is not persistent: "+node);
		}

		final NsTree remove_node = treeRepo.findOne(node.getId());
		final NsTree root = (NsTree)remove_node.getRoot();
		if(remove_node.isRoot()){
			treeRepo.updateRootNull(root, node.getLeft(),node.getRight());
		}
		
		treeRepo.delete(root,remove_node.getLeft(),remove_node.getRight());
		treeRepo.updateDelLeftGap(root,remove_node.getSubTreeSize()*2,remove_node.getLeft());
		treeRepo.updateDelRightGap(root,remove_node.getSubTreeSize()*2,remove_node.getRight());
	}
	@Override
	public void remove(int id) {
		NsTree del_node = treeRepo.findOne(id);
		remove(del_node);
	}
	
	@Override
	public void move(NsTree parent, NsTree node)  {
		Location location = location(parent, UNDEFINED_POSITION, null, ActionType.MOVE);
		int dist = parent.getRight() - node.getLeft() ;
		move(location, node,dist,parent.getDepth() + 1);
	}
	
	@Override
	public void moveBefore(NsTree node, NsTree sibling)  {
		Location location = new Location(sibling.getRoot(), TreeActionLocation.RelatedNodeType.SIBLING, sibling, TreeActionLocation.ActionType.MOVE, sibling.getLeft());
		
		int dist = sibling.getLeft() - node.getLeft() ;
		move(location, node,dist,sibling.getDepth());
	}
	//@Transactional
	private void move(Location location, NsTree node,int dist,int depth) {
		
		NsTree root = (NsTree)location.root;
		int subTreeSize = node.getSubTreeSize();
		
		int range = dist;
		int left = node.getLeft();
		int right = node.getRight();
		
		if(dist < 0){
			range -= subTreeSize * 2;
			left  += subTreeSize * 2;
			right += subTreeSize * 2;
		}
		
		changeDepth(node, depth);
		
		treeRepo.updateAddLeftGap(root,subTreeSize*2,location.targetLeft);
		treeRepo.updateAddRightGap(root,subTreeSize*2, location.targetLeft);
		
		treeRepo.updateRangeLeft(root,range,left,right);
		treeRepo.updateRangeRight(root,range, left,right);
//		
		treeRepo.updateDelLeftGap(root,subTreeSize*2,right);
		treeRepo.updateDelRightGap(root,subTreeSize*2,right);
	}
	
	@Override
	public NsTree copy(NsTree node, NsTree parent, NsTree copiedNodeTemplate)
			 {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NsTree copyTo(NsTree node, NsTree parent, int position, NsTree copiedNodeTemplate)
			 {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NsTree copyBefore(NsTree node, NsTree sibling, NsTree copiedNodeTemplate)
			 {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Location location(NsTree parent, int position, NsTree node, ActionType actionType){
		
		if(parent.isLeaf()){
			return new Location(parent.getRoot(), RelatedNodeType.PARENT, parent, actionType, parent.getLeft() + 1);
		}
		
		if (position <= UNDEFINED_POSITION){
			return new Location(parent.getRoot(), TreeActionLocation.RelatedNodeType.PARENT, parent, actionType, parent.getRight());
		}
		
		 
		List<NsTree> children = findChildren(parent);
		if (position >= children.size()){
			return new Location(parent.getRoot(), TreeActionLocation.RelatedNodeType.PARENT, parent, actionType, parent.getRight());
		}
		
		NsTree sibling = getSiblingNode(children,node,position);
		return new Location(parent.getRoot(), RelatedNodeType.SIBLING, sibling, actionType, sibling.getLeft());
	}
	
	private NsTree getSiblingNode(List<NsTree> children,NsTree moveNode, int position) {
		NsTree sibling = null;
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
		public Location(NestedSetsTreeNode root, RelatedNodeType relatedNodeType, NestedSetsTreeNode relatedNode, ActionType actionType, int targetLeft) {
			super(root, relatedNodeType, relatedNode, actionType);
			this.targetLeft  = targetLeft;
		}
	}


}
