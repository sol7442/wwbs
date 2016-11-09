package com.sol.wwbs.util.tree;

public interface NestedSetsTreeNode extends TreeNode
{
	int getLeft();
	void setLeft(int left);
	int getRight();
	void setRight(int right);
	NestedSetsTreeNode getRoot();
	
	void setRoot(NestedSetsTreeNode root);
	public boolean isRoot();
	public boolean isPersistent();
	public boolean isLeaf();
	
	@Override
	NestedSetsTreeNode clone();
}
