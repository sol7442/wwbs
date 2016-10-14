package com.sol.wwbs.util.tree;

public interface NestedSetsTreeNode extends TreeNode {
	int getLeft();
	void setLeft(int lft);
	int getRight();
	void setRight(int rgt);
	NestedSetsTreeNode getTopLevel();
	void setTopLevel(NestedSetsTreeNode topLevel);
	
	@Override
	NestedSetsTreeNode clone();
}
