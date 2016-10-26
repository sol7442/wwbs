package dao;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sol.wwbs.config.RepositoryConfig;
import com.sol.wwbs.model.NamedNearSetTree;
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
			NamedNearSetTree root = new NamedNearSetTree();
			root.setName("Admin");
			taskService.createRoot(root);
		} catch (UniqueConstraintViolationException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testAddTask(){
		try {
			NamedNearSetTree root = taskService.getRoot("Admin");
			NamedNearSetTree sub = new NamedNearSetTree();
			sub.setName("Main5");
			
			sub = taskService.addChild(root, sub);
			
			System.out.println(root);
			System.out.println(sub);
			
		} catch (UniqueConstraintViolationException e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void testAddTenSubTask(){
		try {
			NamedNearSetTree sub;// = new TaskTree();
			for(int i=0;i<10;i++){
				NamedNearSetTree root = taskService.getRoot("Admin");

				sub = new NamedNearSetTree();
				sub.setName("Sub" + i);
				sub = taskService.addChild(root, sub);
			}
			
			testGetChildTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void testAddFiveDepthSubTask(){
		try {
			NamedNearSetTree root = taskService.getRoot("Admin");
			List<NamedNearSetTree> sub_list = taskService.getChildren(root);
			for(NamedNearSetTree entity : sub_list){
				List<NamedNearSetTree> sub_tree_list = taskService.getTree(entity.getName());
				NamedNearSetTree add_tree = new NamedNearSetTree();
				add_tree.setName(entity.getName() + entity.getId());
				NamedNearSetTree sub_tree0 = sub_tree_list.get(0);

				taskService.addChild(sub_tree0, add_tree);
			}
			
			testGetChildTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChildTask(){
		try {
			NamedNearSetTree root = taskService.getRoot("Admin");
			List<NamedNearSetTree> list = taskService.getChildren(root);
			
			System.out.println(root);
			for(NamedNearSetTree tree : list){
				String depth_str = "";
				for(int i=0;i<tree.getDepth();i++){
					depth_str +="-";
				}
				System.out.println(depth_str + tree);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testRemoveTask(){
		try {
			NamedNearSetTree root = taskService.getRoot("Admin");
			List<NamedNearSetTree> list = taskService.getChildren(root);
			for(NamedNearSetTree tree : list){
				System.out.println(tree);
			}
			
			NamedNearSetTree del_task = list.get(list.size() - 1);
			taskService.remove(del_task);
		
			list = taskService.getChildren(root);
			for(NamedNearSetTree tree : list){
				System.out.println(tree);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	//@Test
//	public void testRemoveByNameTask(){
//		try {
//			TaskTree root = taskService.getRoot("Admin");
//			List<TaskTree> list = taskService.getChildren(root);
//			for(TaskTree tree : list){
//				System.out.println(tree);
//			}
//			
//			TaskTree del_task = list.get(list.size() - 1);
//			taskService.removeByName(del_task.getName());
//		
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//@Test
//	public void testRemoveByRangeTask(){
//		try {
//			TaskTree root = taskService.getRoot("Admin");
//			List<TaskTree> list = taskService.getChildren(root);
//			for(TaskTree tree : list){
//				System.out.println(tree);
//			}
//			
//			TaskTree del_task = list.get(list.size() - 1);
//			taskService.removeByRange(del_task.getLeft(), del_task.getRight());
//		
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	///@Test
//	public void testRemameTask(){
//		try {
//			TaskTree root = taskService.getRoot("Admin");
//			List<TaskTree> list = taskService.getChildren(root);
//			for(TaskTree tree : list){
//				System.out.println(tree);
//			}
//			
//			TaskTree del_task = list.get(list.size() - 1);
//			del_task.setName("ReNamed");
//			taskService.rename(del_task);
//		
//			list = taskService.getChildren(root);
//			for(TaskTree tree : list){
//				System.out.println(tree);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testFindRoot(){
//		List<TaskTree> roots = taskService.getRoots();
//		for(TaskTree taktree : roots){
//			System.out.println(taktree.getTaskId() + ":("+taktree.getLeft()+")("+taktree.getRight()+")");
//		}
//	}
}
