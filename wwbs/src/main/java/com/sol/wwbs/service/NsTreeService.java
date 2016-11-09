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
	public NsTree get(int id) {
		return treeRepo.findOne(id);
	}
//	public List<NsTree> find(String name) {
//		return treeRepo.findByTreeName(name);
//	}
	
	public List<NsTree> getAll(NsTree root) {
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
	public List<NsTree> getRoots() {
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
	public List<NsTree> getSubTree(NsTree parent) {
		if(parent == null){
			return new ArrayList<>();
		}
		return treeRepo.findSubTree((NsTree)parent.getRoot(),parent.getLeft(),parent.getRight());
	}
	@Override
	public List<NsTree> getChildren(NsTree parent) {
		if(parent == null){
			return new ArrayList<>();
		}
		return treeRepo.findChildren((NsTree)parent.getRoot(),parent.getLeft(),parent.getRight(),parent.getDepth() + 1);
	}
	
	@Override
	public NsTree getParent(NsTree node) {
		if(node.isRoot()) return null;
		return treeRepo.findParent(node.getRoot(),node.getLeft(),node.getRight(),node.getDepth() - 1);
	}
	@Override
	public List<NsTree> getPath(NsTree node) {
		return treeRepo.getPath(node.getRoot(),node.getLeft(),node.getRight());
	}
	
	@Override
	@Transactional
	public NsTree addChild(NsTree parent, NsTree child)  {
		return addChildAt(parent, child, UNDEFINED_POSITION);
	}
	
	@Override
	@Transactional
	public NsTree addChildAt(NsTree parent, NsTree child, int position){
		final NsTree reflash_parent = reflash(parent);
		Location location = location(reflash_parent, position, null, ActionType.INSERT);
		return addChild(child,location,reflash_parent.getDepth() + 1);
	}
	
	@Override
	public NsTree reflash(NsTree node) {
		return treeRepo.findOne(node.getId());
	}

	@Override
	@Transactional
	public NsTree addChildBefore(NsTree sibling, NsTree child)  {
		final NsTree reflash_sibling = reflash(sibling);
		
		Location location = new Location(
				reflash_sibling.getRoot(),
				RelatedNodeType.SIBLING, reflash_sibling, ActionType.INSERT, reflash_sibling.getLeft());
		
		return addChild(child,location,reflash_sibling.getDepth());
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
		
		treeRepo.updateAddLeftGap(root,2,location.targetLeft);
		treeRepo.updateAddRightGap(root,2, location.targetLeft);
		
		return treeRepo.save(child);
	}
	
	private void changeDepth(NsTree node,int depth){
		List<NsTree> children =  getChildren(node);
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
	@Transactional
	public void remove(NsTree node) {
		if(node == null ){
			throw new IllegalArgumentException("Node is null  "+node);
		}
		
		if(!node.isPersistent()){
			throw new IllegalArgumentException("Node is not persistent: "+node);
		}

		final NsTree reflash_node = reflash(node);
		final NsTree root = (NsTree)reflash_node.getRoot();
		if(reflash_node.isRoot()){
			treeRepo.updateRootNull(root, node.getLeft(),node.getRight());
		}
		
		treeRepo.delete(root,reflash_node.getLeft(),reflash_node.getRight());
		treeRepo.updateDelLeftGap(root,reflash_node.getSubTreeSize()*2,reflash_node.getLeft());
		treeRepo.updateDelRightGap(root,reflash_node.getSubTreeSize()*2,reflash_node.getRight());
	}
	@Override
	public void remove(int id) {
		NsTree del_node = treeRepo.findOne(id);
		remove(del_node);
	}
	
	@Override
	@Transactional
	public void move(NsTree parent, NsTree node)  {
		final NsTree reflash_parent = reflash(parent);
		final NsTree reflash_node = reflash(node);
		
		Location location = location(reflash_parent, UNDEFINED_POSITION, null, ActionType.MOVE);
		int dist = reflash_parent.getRight() - reflash_node.getLeft() ;
		move(location, reflash_node,dist,reflash_parent.getDepth() + 1);
	}
	
	@Override
	@Transactional
	public void moveBefore(NsTree node, NsTree sibling)  {
		final NsTree reflash_sibling = reflash(sibling);
		final NsTree reflash_node = reflash(node);
		
		Location location = new Location(reflash_sibling.getRoot(), 
				TreeActionLocation.RelatedNodeType.SIBLING, reflash_sibling, 
				TreeActionLocation.ActionType.MOVE, reflash_sibling.getLeft());
		
		int dist = reflash_sibling.getLeft() - reflash_node.getLeft() ;
		move(location, reflash_node,dist,reflash_sibling.getDepth());
	}
	
	private void move(Location location, NsTree node,int dist,int depth) {
		//TODO - validate move node position
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
		
		 
		List<NsTree> children = getChildren(parent);
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
