package dao;


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
	
	@Test
	public void testAddTask(){
		try {
			TaskTree root = taskService.getRoot("Admin");
			TaskTree sub = new TaskTree();
			sub.setName("Main");
			
			System.out.println(root);
			
			taskService.addChild(root, sub);
		} catch (UniqueConstraintViolationException e) {
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
