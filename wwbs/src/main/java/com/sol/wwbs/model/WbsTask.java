package com.sol.wwbs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="_task")
public class WbsTask implements Serializable{
	private static final long serialVersionUID = 1L;

	public enum TYPE{
		Control,View,Model,Architechtrue,Envementment,Document,Research,Support,Education
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	private String name;
	
	private String desc;
	
	@OneToOne
	@JoinColumn(name="treeId")
	private NsTree tree;
	
	public void setTaskTree(NsTree tree){
		this.tree = tree;
	}
	public NsTree getTaskTree(){
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
