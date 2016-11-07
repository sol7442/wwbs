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
			sub.setName("sub"+root.getDepth());
			
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
	public void testAddNearTask(){
		try {
			
			List<NamedNearSetTree> sub_tree_list = taskService.findByName("Sub020");
			NamedNearSetTree sub_tree0 = sub_tree_list.get(0);
			NamedNearSetTree new_tree0 = new NamedNearSetTree(); 
			new_tree0.setName("NearTree");
			new_tree0 = taskService.addChildBefore(sub_tree0, new_tree0);
			
			System.out.println(new_tree0);
			
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
				List<NamedNearSetTree> sub_tree_list = taskService.findByName(entity.getName());
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
	
	//@Test
	public List<NamedNearSetTree> testGetChildTask(){
		List<NamedNearSetTree> list = null;
		
		try {
			NamedNearSetTree root = taskService.getRoot("Admin");
			list = taskService.getChildren(root);
			
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
		
		return list;
	}
	
	//@Test
	public void testRemoveTask(){
		testGetChildTask();
		try {
			NamedNearSetTree del_task = taskService.find(45);
			if(del_task != null){
				taskService.remove(45);
			}
			testGetChildTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
		testGetChildTask();
	}
	
	//@Test
	public void testMoveTaskBefore1(){
		testGetChildTask();
		List<NamedNearSetTree> targets1 = taskService.findByName("Sub525");
		List<NamedNearSetTree> targets2 = taskService.findByName("Sub626");
		try {
			NamedNearSetTree target	 = targets1.get(0);
			NamedNearSetTree before  = targets2.get(0);
			
			System.out.println("taget : " + target);
			System.out.println("before: " + before);
			
			taskService.moveBefore(target, before);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testGetChildTask();
	}
	
	//@Test
	public void testMoveTaskBefore2(){
		testGetChildTask();
		NamedNearSetTree target = taskService.find(29);
		NamedNearSetTree before = taskService.find(34);
		try {
			System.out.println("taget : " + target);
			System.out.println("before: " + before);
			
			taskService.moveBefore(target, before);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testGetChildTask();
	}
	
	//@Test
	public void testMoveTaskAfter1(){
		testGetChildTask();
		NamedNearSetTree target = taskService.find(38);
		NamedNearSetTree before = taskService.find(33);
		try {
			System.out.println("taget : " + target);
			System.out.println("before: " + before);
			
			taskService.moveBefore(target, before);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testGetChildTask();
	}
	
	public void testMoveTask(){
		testGetChildTask();
		NamedNearSetTree target = taskService.find(38);
		NamedNearSetTree parent = taskService.find(33);
		try {
			System.out.println("taget : " + target);
			System.out.println("parent: " + parent);
			
			taskService.move(parent,target);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testGetChildTask();
	}
}
