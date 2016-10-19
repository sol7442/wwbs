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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RepositoryConfig.class)
@WebAppConfiguration
public class NstTreeTest {
	
	@Autowired
	private TaskService taskService;
	
	@Test
	public void testCreate(){
		TaskTree tsktre = new TaskTree();
		taskService.create(tsktre);
	}
	
	@Test
	public void testRoot(){
		TaskTree root = new TaskTree();
		taskService.createRoot(root);
	}
	
	@Test
	public void testFindRoot(){
		List<TaskTree> roots = taskService.getRoots();
		
		for(TaskTree taktree : roots){
			System.out.println(taktree.getTaskId() + ":("+taktree.getLeft()+")("+taktree.getRight()+")");
		}
	}
}
