package com.sol.wwbs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="_task")
public class Task implements Serializable{
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	private String name;
	
	private String desc;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="treeId")
	private TaskTree tree;
	
	
	public void setTaskTree(TaskTree tree){
		this.tree = tree;
	}
	public TaskTree getTaskTree(){
		return this.tree;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}
}
