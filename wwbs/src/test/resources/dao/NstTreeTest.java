package dao;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sol.wwbs.config.RepositoryConfig;
import com.sol.wwbs.model.TaskTree;
import com.sol.wwbs.service.TaskService;
import com.sol.wwbs.util.tree.UniqueConstraintViolationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RepositoryConfig.class)
@WebAppConfiguration
public class NstTreeTest {
	
	@Autowired
	private TaskService taskService;
	
	
	//@Test
	public void testCreateRoot(){
		try {
			TaskTree root = new TaskTree();
			root.setName("Admin");
			taskService.createRoot(root);
		} catch (UniqueConstraintViolationException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testAddTask(){
		try {
			TaskTree root = taskService.getRoot("Admin");
			TaskTree sub = new TaskTree();
			sub.setName("Main5");
			
			sub = taskService.addChild(root, sub);
			
			System.out.println(root);
			System.out.println(sub);
			
		} catch (UniqueConstraintViolationException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testGetChildTask(){
		try {
			TaskTree root = taskService.getRoot("Admin");
			List<TaskTree> list = taskService.getChildren(root);
			
			for(TaskTree tree : list){
				System.out.println(tree);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRemoveTask(){
		try {
			TaskTree root = taskService.getRoot("Admin");
			List<TaskTree> list = taskService.getChildren(root);
			for(TaskTree tree : list){
				System.out.println(tree);
			}
			
			TaskTree del_task = list.get(list.size() - 1);
			taskService.remove(del_task);
		
			list = taskService.getChildren(root);
			for(TaskTree tree : list){
				System.out.println(tree);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testFindRoot(){
//		List<TaskTree> roots = taskService.getRoots();
//		for(TaskTree taktree : roots){
//			System.out.println(taktree.getTaskId() + ":("+taktree.getLeft()+")("+taktree.getRight()+")");
//		}
//	}
}
