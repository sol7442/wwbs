package com.sol.wwbs.model;

import java.io.Serializable;

import javax.persistence.*;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;


/**
 * The persistent class for the TaskTree database table.
 * 
 */
@Entity
@Table(name="_tree")
public class NsTree implements Serializable, NestedSetsTreeNode {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id = -1;
	
//	private String name;
	
	@Column(name="lft")
	private int left;
	
	@Column(name="rgt")
	private int right;
	
	@Column(name="depth")
	private int depth;

//	@OneToMany(mappedBy = "tree", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<Task> tasks;
	
	@ManyToOne(targetEntity = NsTree.class)
	@JoinColumn(name="root", nullable = true, updatable = false)
	private NsTree root;

	
	public NsTree() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubTreeSize(){
		return (this.right - this.left)/2 + 1;
	}
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	@Override
	public int getLeft() {
		return this.left;	
	}

	@Override
	public void setLeft(int lft) {
		this.left = lft;
	}

	@Override
	public int getRight() {
		return this.right;
	}

	@Override
	public void setRight(int rgt) {
		this.right = rgt;
	}

	@Override
	public NestedSetsTreeNode getRoot() {
		return this.root;
	}

	@Override
	public void setRoot(NestedSetsTreeNode root) {
		this.root = (NsTree)root;
	}

	@Override
	public NestedSetsTreeNode clone() {
		NsTree clone = new NsTree();
		clone.setRoot(getRoot());
		return clone;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
	
	private String getNodeDepthString() {
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<this.depth;i++){
			buffer.append("-");
		}
		return buffer.toString();
	}
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(getNodeDepthString());
		buffer.append("[").append(this.root.getId()).append("]");
		buffer.append("[").append(this.id).append("]");
//		buffer.append("[").append(this.name).append("]");
		buffer.append("(").append(this.depth).append(")");
		buffer.append(":").append(this.left).append(",").append(this.right);
		
		return buffer.toString();
	}

	@Override
	public boolean isRoot() {
		if(root == null){
			return false;
		}
		return this.getId() == root.getId();
	}

	@Override
	public boolean isPersistent() {
		return this.id > 0;
	}
	@Override
	public boolean isLeaf() {
		return this.left + 1 == this.right;
	}

}