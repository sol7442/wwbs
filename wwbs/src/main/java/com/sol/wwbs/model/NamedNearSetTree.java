package com.sol.wwbs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;


/**
 * The persistent class for the TaskTree database table.
 * 
 */
@Entity
@Table(name="_tree")
public class NamedNearSetTree implements Serializable, NestedSetsTreeNode {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id = -1;
	
	private String name;
	
	@Column(name="lft")
	private int left;
	
	@Column(name="rgt")
	private int right;
	
	@Column(name="depth")
	private int depth;

//	@OneToMany(mappedBy = "tree", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<Task> tasks;
	
	@ManyToOne(targetEntity = NamedNearSetTree.class)//, optional=true, fetch=FetchType.LAZY, cascade = CascadeType.REFRESH  )
	@JoinColumn(name="root", nullable = true, updatable = true)
	private NamedNearSetTree root;

	
	public NamedNearSetTree() {
	}

	public int getTaskId() {
		return this.id;
	}

	public void setTaskId(int id) {
		this.id = id;
	}

	public int numberOfNodesInSubTree(){
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
		this.root = (NamedNearSetTree)root;
	}

	@Override
	public NestedSetsTreeNode clone() {
		NamedNearSetTree clone = new NamedNearSetTree();
		clone.setRoot(getRoot());
		return clone;
	}

	@Override
	public Serializable getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

//	public List<Task> getTasks() {
//		return tasks;
//	}
//
//	public void addTasK(Task task) {
//		if(this.tasks == null){
//			this.tasks = new ArrayList<Task>();
//		}
//		this.tasks.add(task);
//	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[").append(this.root.getId()).append("]");
		buffer.append("[").append(this.id).append("]");
		buffer.append("[").append(this.name).append("]");
		buffer.append(":").append(this.left).append(",").append(this.right);
		
		return buffer.toString();
	}

	@Override
	public boolean isRoot() {
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