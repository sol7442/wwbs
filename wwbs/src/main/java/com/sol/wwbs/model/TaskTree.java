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
public class TaskTree implements Serializable, NestedSetsTreeNode {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Column(name="lft")
	private int left;
	
	@Column(name="rgt")
	private int right;

	@OneToMany(mappedBy = "tree")
	private List<Task> tasks;
	
	@ManyToOne(targetEntity = TaskTree.class)
	@JoinColumn(name="root")
	private NestedSetsTreeNode root;

	
	public TaskTree() {
	}

	public int getTaskId() {
		return this.id;
	}

	public void setTaskId(int id) {
		this.id = id;
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
		this.root = root;
	}

	@Override
	public NestedSetsTreeNode clone() {
		TaskTree clone = new TaskTree();
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
	

	public List<Task> getTasks() {
		return tasks;
	}

	public void addTasK(Task task) {
		if(this.tasks == null){
			this.tasks = new ArrayList<Task>();
		}
		this.tasks.add(task);
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[").append(this.id).append("]");
		buffer.append("[").append(this.name).append("]");
		buffer.append(":").append(this.left).append(",").append(this.right).append(",");
		buffer.append(this.root);
		
		return buffer.toString();
	}

}