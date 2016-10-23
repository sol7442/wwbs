package com.sol.wwbs.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.wwbs.model.TaskTree;
import com.sol.wwbs.model.Task;
import com.sol.wwbs.repository.TaskRepository;
import com.sol.wwbs.repository.TaskTreeRepository;
import com.sol.wwbs.util.tree.TreeActionLocation;
import com.sol.wwbs.util.tree.TreeNode;


@Service
public class TaskService {
	@Autowired
	private TaskTreeRepository treeRepo;
	private TaskRepository taskRepo;
	
	private static final int ROOT_LEFT  = 1;
	private static final int ROOT_RIGHT = 2;
	
	public TaskTree createTaskTree(TaskTree tree){
		return treeRepo.save(tree);
	}
	public TaskTree createRoot(TaskTree root){
		root.setLeft(ROOT_LEFT);
		root.setRight(ROOT_RIGHT);
		root.setTopLevel(root);
		
		//Location location = new Location(null, TreeActionLocation.RelatedNodeType.PARENT, null, TreeActionLocation.ActionType.INSERT, ROOT_LEFT);
		//checkUniqueness(Arrays.asList(new NestedSetsTreeNode [] { root }), location);
		
		return treeRepo.save(root);
		
	}
	public Task createTask(TaskTree tree, Task task){
		task = taskRepo.save(task);
		tree = treeRepo.save(tree);
		
		return task;
	}
	
//	public TaskTree create(TaskTree tskTree){
//		if(treeRepo.exists(tskTree.getTaskId())){
//			System.out.println("exe---");			
//		}
//		return treeRepo.save(tskTree);
//	}
//	
//	public TaskTree find(int id){
//		return treeRepo.getOne(id);
//	}
//	
//	public void update(TaskTree tskTree){
//		treeRepo.save(tskTree);
//	}
//	
//	public TaskTree createRoot(TaskTree root){
//		root.setLeft(ROOT_LEFT);
//		root.setRight(ROOT_RIGHT);
//		return treeRepo.save(root);
//	}
//	
//	public List<TaskTree> getRoots(){
//		return treeRepo.findRoots();
//	}
	
	private void checkUniqueness(List<TreeNode> nodes, TreeActionLocation<TreeNode> location) {
	
		
//		if (getUniqueTreeConstraint() == null)
//			return;	// nothing to check
//		
//		assert nodes != null && nodes.size() >= 1 && location != null : "Need node and location to check unique constraint!";
//		
//		getUniqueTreeConstraint().setContext(session, this, nodeEntityName(), pathEntityName());
//		
//		if (getUniqueTreeConstraint().checkUniqueConstraint(nodes, location) == false)	{
//			// there is no chance to recover the old state of the updated entity from database,
//			// as the check-query triggered a flush() and updates are already in transaction
//			
//			String message = "One of following entities is not unique: "+nodes;	// create error message BEFORE refresh
//			@SuppressWarnings("unchecked")
//			N clone = (N) nodes.get(0).clone();	// assume the first node is invalid
//			throw new UniqueConstraintViolationException(message, clone);
//		}
	}
	
	private static class Location extends TreeActionLocation<TreeNode>{
		public final int targetLeft;
		public Location(TreeNode root, RelatedNodeType relatedNodeType, TreeNode relatedNode, ActionType actionType, int targetLeft) {
			super(root, relatedNodeType, relatedNode, actionType);
			this.targetLeft = targetLeft;
		}
	}
}
