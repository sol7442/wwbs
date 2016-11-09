package com.sol.wwbs.util.tree;
import java.util.List;

public interface TreeDao<N extends TreeNode> 
{
	int UNDEFINED_POSITION = -1;	// must be below allowed positions which start at zero
	
	N reflash(N node);
	N get(int id);
	N getParent(N node);	
	List<N> getRoots();
	List<N> getPath(N node);
	List<N> getChildren(N parent);
	List<N> getSubTree(N parent);
	
	N addChild(N parent, N child) ;
	N addChildAt(N parent, N child, int position) ;
	N addChildBefore(N sibling, N child);
	
	N update(N entity) ;
	N createRoot(N root) ;
	void removeAll();
	void remove(N node);
	void remove(int id);

	void move(N newParent , N node) ;
	void moveBefore(N node, N sibling) ;

	N copy(N node, N parent, N copiedNodeTemplate) ;
	N copyTo(N node, N parent, int position, N copiedNodeTemplate) ;
	N copyBefore(N node, N sibling, N copiedNodeTemplate) ;
}
