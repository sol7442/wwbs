package com.sol.wwbs.model;

import java.io.Serializable;
import javax.persistence.*;
import com.sol.wwbs.util.tree.NestedSetsTreeNode;


/**
 * The persistent class for the TaskTree database table.
 * 
 */
@Entity
@Table(name="TaskTree")
//@NamedQuery(name="TaskTree.findAll", query="SELECT t FROM TaskTree t")
public class TaskTree implements Serializable, NestedSetsTreeNode {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int taskId;

	private int lft;

	private int rgt;

	//bi-directional many-to-one association to TskTree
	@ManyToOne(targetEntity=TaskTree.class)
	@JoinColumn(name="topLevel")
	private NestedSetsTreeNode topLevel;

	public TaskTree() {
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	@Override
	public int getLeft() {
		return this.lft;	
	}

	@Override
	public void setLeft(int lft) {
		this.lft = lft;
	}

	@Override
	public int getRight() {
		return this.rgt;
	}

	@Override
	public void setRight(int rgt) {
		this.rgt = rgt;
	}

	@Override
	public NestedSetsTreeNode getTopLevel() {
		return this.topLevel;
	}

	@Override
	public void setTopLevel(NestedSetsTreeNode topLevel) {
		this.topLevel = topLevel;
	}

	@Override
	public NestedSetsTreeNode clone() {
		TaskTree clone = new TaskTree();
		clone.setTopLevel(getTopLevel());
		return clone;
	}

}