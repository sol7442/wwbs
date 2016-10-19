package com.sol.wwbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.wwbs.model.TaskTree;
import com.sol.wwbs.repository.TaskTreeRepository;

@Service
public class TaskService {
	@Autowired
	private TaskTreeRepository taskTreeRepository;
	
	private static final int ROOT_LEFT  = 1;
	private static final int ROOT_RIGHT = 2;
	
	
	public TaskTree create(TaskTree tskTree){
		if(taskTreeRepository.exists(tskTree.getTaskId())){
			System.out.println("exe---");			
		}
		return taskTreeRepository.save(tskTree);
	}
	
	public TaskTree find(int id){
		return taskTreeRepository.getOne(id);
	}
	
	public void update(TaskTree tskTree){
		taskTreeRepository.save(tskTree);
	}
	
	public TaskTree createRoot(TaskTree root){
		root.setLeft(ROOT_LEFT);
		root.setRight(ROOT_RIGHT);
		return taskTreeRepository.save(root);
	}
	
	public List<TaskTree> getRoots(){
		return taskTreeRepository.findRoots();
	}
}
