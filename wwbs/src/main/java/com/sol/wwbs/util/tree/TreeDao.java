package com.sol.wwbs.util.tree;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * Tree DAO provides hierarchical access to records in a JPA database layer.
 * There can be more than one tree in a database table.
 * A record can not be in several trees at the same time.
 * A record can have one parent or no parent (root), but not several parents.
 * Normally there is no record that is not in a tree, except it is a root without children.
 * <p/>
 * This interface abstracts the functionality of a tree DAO so that the underlying
 * implementation can be replaced. In this library two such DAOs are implemented, 
 * one for a nested-sets-tree, one for a closure-table-tree.
 * <p/>
 * For more information see
 * <a href="http://de.slideshare.net/billkarwin/models-for-hierarchical-data">Bill Karwin slides "Models for Hierarchical Data"</a>, page 40, and
 * <a href="http://de.slideshare.net/billkarwin/sql-antipatterns-strike-back">Bill Karwin slides "SQL Antipatterns strike back"</a>, page 68.
 * 
 * @author Fritz Ritzberger, 19.10.2012
 * 
 * @param N the tree node type managed by this DAO.
 */
public interface TreeDao<N extends TreeNode> 
{
	/** The position parameter to express "append to end of parent's child list", for add, move, copy. */
	int UNDEFINED_POSITION = -1;	// must be below allowed positions which start at zero
	
	
	/** @return true if passed entity is already persistent, i.e. its getId() is not null. */
	//boolean isPersistent(N entity);

	/** @return the object by identity (primary key) from database. */
	N find(int id);
	
	/**
	 * Updates the given persistent object. This performs explicit constraint checking
	 * when <code>checkUniqueConstraintsOnUpdate</code> is true (default is false).
	 * @ when given entity is not unique.
	 * @throws IllegalArgumentException when given node is not yet persistent.
	 */
	N update(N entity) ;

//	/** @return true if passed node is persistent and a root. */
//	boolean isRoot(N entity);

	/**
	 * Creates a tree root node.
	 * @ when uniqueness would be violated.
	 * @throws IllegalArgumentException when root is already persistent.
	 */
	N createRoot(N root) ;

//	/** @return the count of all nodes of any depth below given node, including itself. */
//	int size(N tree);

	/** @return all tree root nodes. */
	List<N> findRoots();

	/** Removes all roots, including the nodes below them. Thus clears the table. */
	void removeAll();

//	/**
//	 * Reads a tree or sub-tree, including all children.
//	 * The result is NOT EXPECTED to be used with
//	 * <code>findSubTree()</code> or <code>findDirectChildren()</code>!
//	 * @param parent the parent of the tree to read, can also be root of the tree.
//	 * @return all tree nodes under given parent, including parent.
//	 */
//	List<N> getTree(N parent);
//
//	/**
//	 * Reads a tree or sub-tree, including all children,
//	 * which can be cached and passed back into
//	 * <code>findSubTree()</code> or <code>findDirectChildren()</code>.
//	 * Mind that any cached tree could be out-of-sync with database when another client performs changes.
//	 * @param parent the parent of the tree to read, can also be root of the tree.
//	 * @return all tree nodes under given parent, including parent, in depth-first order.
//	 */
//	List<N> getTreeCacheable(N parent);

	/**
	 * Finds a sub-tree list in a cached list of tree nodes under given parent.
	 * The subNodes list was returned from a call to <code>getTreeCacheable()</code>.
	 * Mind that any cached tree could be out-of-sync with database when another client performs changes.
	 * @param parent the parent node to search a sub-tree for, contained somewhere in the given list of nodes.
	 * @param treeCacheable a list of nodes from which to extract a sub-tree, also containing given parent.
	 * @return a list of nodes under the passed parent node.
	 */
	List<N> findSubTree(N parent);

//	/** @return true when given node has no children, i.e. is not a container-node. */
//	boolean isLeaf(N node);
//
//	/** @return the number of direct children of given parent node. */
//	int getChildCount(N parent);

	/**
	 * Gives the children of passed parent. This method reads the full subtree under parent.
	 * Removing from returned list will not remove that child from tree but cause an exception.
	 * @return the ordered list of <b>direct</b> children under given parent.
	 */
	List<N> findChildren(N parent);
//
//	/** @return the root node of given node. Root has itself as root. */
//	N findRoot(N node);

	/** @return the parent node of given node. Root has null as parent. */
	N findParent(N node);

	/** @return all parent nodes of given node, i.e. its path from root to (exclusive) node. Root will return an empty list. */
	List<N> getPath(N node);


	/**
	 * Adds to end of children of given parent. 
	 * @ when uniqueness would be violated.
	 */
	N addChild(N parent, N child) ;

	/**
	 * Adds at specified position to children of given parent.
	 * @param position -1 for append, else target position in child list.
	 * @ when uniqueness would be violated.
	 */
	N addChildAt(N parent, N child, int position) ;

	/**
	 * Adds to children before given sibling, sibling is pushed backwards in children list. 
	 * @ when uniqueness would be violated.
	 */
	N addChildBefore(N sibling, N child) ;

	/** Removes the tree under given node, including the node. Node can also be a root. */
	void remove(N node);
	void remove(int id);
	/**
	 * Moves the given node to end of children list of parent.
	 * When source is identical with target, nothing happens.
	 * @ 
	 * @throws IllegalArgumentException when target is below source.
	 */
	void move(N node, N newParent) ;

//	/**
//	 * Moves the given node to given position in children list of parent.
//	 * When source is identical with target, nothing happens.
//	 * @ 
//	 * @throws IllegalArgumentException when target is below source.
//	 */
//	void moveTo(N node, N parent, int position) ;

	/**
	 * Moves the given node to position of given sibling, pushing sibling backwards in list.
	 * When source is identical with target, nothing happens.
	 * @ 
	 * @throws IllegalArgumentException when target is below source.
	 */
	void moveBefore(N node, N sibling) ;


	/**
	 * Copies the given node to end of children list of parent.
	 * When source is identical with target, the node will be be copied to the position of its originator.
	 * @param node the node to be copied.
	 * @param copiedNodeTemplate a template for the copied node containing altered properties, can be null.
	 * @ when uniqueness would be violated.
	 * @throws IllegalArgumentException when target is below source.
	 */
	N copy(N node, N parent, N copiedNodeTemplate) ;

	/**
	 * Copies the given node to given position in children list of parent. 
	 * When source is identical with target, the node will be be copied to the position of its originator.
	 * @param node the node to be copied.
	 * @param parent the parent-node of the children the node should be copied into.
	 * @param position the 0-n index the node should obtain within children of parent.
	 * @param copiedNodeTemplate a template for the copied node containing altered properties, can be null.
	 * @ when uniqueness would be violated.
	 * @throws IllegalArgumentException when target is below source.
	 */
	N copyTo(N node, N parent, int position, N copiedNodeTemplate) ;

	/**
	 * Copies the given node to position of given sibling, pushing sibling backwards in list.
	 * When source is identical with target, the node will be be copied to the position of its originator.
	 * @param node the node to be copied.
	 * @param sibling the target node the copied node should push backwards in children list.
	 * @param copiedNodeTemplate a template for the copied node containing altered properties, can be null.
	 * @ when uniqueness would be violated.
	 * @throws IllegalArgumentException when target is below source.
	 */
	N copyBefore(N node, N sibling, N copiedNodeTemplate) ;

	
}
