package dao;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sol.wwbs.config.RepositoryConfig;
import com.sol.wwbs.model.NsTree;
import com.sol.wwbs.util.tree.TreeDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RepositoryConfig.class)
@WebAppConfiguration
public class NstTaskTest {
	
	@Autowired
	private TreeDao<NsTree> treeService;
	
	
	//@Test
	public void testGetRoots(){
		List<NsTree> root_list = treeService.getRoots();
		System.out.println("=[testGetRoots]=====");
		for(NsTree node : root_list){
			System.out.println(node);
		}
	}
	
	//@Test
	public void testGetTreeById(){
		NsTree root_node = treeService.get(51);
		System.out.println("=[testGetTreeById]=====");
		System.out.println(root_node);
	}
	
	//@Test
	public void testGetChildren(){
		NsTree parent_node = treeService.get(23);
		List<NsTree> node_list = treeService.getChildren(parent_node);
		System.out.println("=[testGetChildren]=====");
		for(NsTree node : node_list){
			System.out.println(node);
		}
	}
	
	//@Test
	public void testGetSubtree(){
		NsTree parent_node = treeService.get(23);
		List<NsTree> node_list = treeService.getSubTree(parent_node);
		System.out.println("=[testGetSubtree]=====");
		for(NsTree node : node_list){
			System.out.println(node);
		}
	}
	
	//@Test
	public void testGetSubtreeError(){
		NsTree parent_node = treeService.get(102);
		List<NsTree> node_list = treeService.getSubTree(parent_node);
		System.out.println("=[testGetSubtreeError]=====");
		for(NsTree node : node_list){
			System.out.println(node);
		}
	}
	
	//@Test
	public void testGetParent(){
		NsTree node = treeService.get(38);
		NsTree parent_node = treeService.getParent(node);
		System.out.println("=[testGetParent]=====");
		System.out.println(parent_node);
	}
	
	//@Test
	public void testGetPath(){
		NsTree node = treeService.get(36);
		List<NsTree> node_path = treeService.getPath(node);
		System.out.println("=[testGetPath]=====");
		for(NsTree path : node_path){
			System.out.println(path);
		}
	}
	
	
	//@Test
	public void testGetAll(){
		NsTree root = treeService.getRoots().get(0);
		List<NsTree> node_path = treeService.getSubTree(root);
		System.out.println(root);
		for(NsTree path : node_path){
			System.out.println(path);
		}
	}
	
	//@Test
	public void testRemove(){
		try{
			List<NsTree> root_list = treeService.getRoots();
			System.out.println("=[Size : ]=====" + root_list.get(0).getSubTreeSize());
			treeService.remove(48);
			root_list = treeService.getRoots();
			System.out.println("=[Size : ]=====" + root_list.get(0).getSubTreeSize());
			testGetAll();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//@Test
	public void testRemove2(){
		try{
			NsTree del_node = treeService.get(49);
			treeService.remove(del_node.getId());
			testGetAll();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//@Test
	public void testAdd(){
		try{
			System.out.println("=[testAdd : ]=====");
			NsTree root =  treeService.get(1);
			NsTree new_parent = new NsTree();
			NsTree new_child = new NsTree();
			NsTree new_subchild = new NsTree();
			NsTree new_sibling1 = new NsTree();
			NsTree new_sibling2 = new NsTree();
			
			new_parent = treeService.addChild(root, new_parent);
			new_child = treeService.addChild(new_parent, new_child);
			new_subchild = treeService.addChild(new_child, new_subchild);
			new_sibling1 = treeService.addChildBefore(new_child, new_sibling1);
			new_sibling2 = treeService.addChildAt(new_parent, new_sibling2, 1);
			
			System.out.println("new_parent :" +  new_parent);
			System.out.println("new_child :" +  new_child);
			System.out.println("new_subchild :" +  new_subchild);
			System.out.println("new_sibling1 :" +  new_sibling1);
			System.out.println("new_sibling2 :" +  new_sibling2);
			
			testGetAll();
			
//			new_parent = treeService.get(new_parent.getId());
			treeService.remove(new_parent);
//			testGetAll();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testMove(){
		try{
			System.out.println("=[testMove : ]=====");
			NsTree root =  treeService.get(1);
			
			testGetAll();
			
			List<NsTree> children = treeService.getChildren(root);
			NsTree sibling  = children.get(1);
			NsTree move_node = children.get(2);
			
			treeService.moveBefore(move_node, sibling);
			NsTree new_child = new NsTree();
			new_child = treeService.addChild(root, new_child);			
			
			treeService.move(sibling, new_child);
			testGetAll();
			
			treeService.remove(new_child);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
//	
//	//@Test
//	public void testCreateRoot(){
//		try {
//			NsTree root = new NsTree();
//			root.setName("Admin");
//			taskService.createRoot(root);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//@Test
//	public void testAddTask(){
//		try {
//			NsTree root = taskService.findRoot("Admin");
//			NsTree sub = new NsTree();
//			sub.setName("sub"+root.getDepth());
//			
//			sub = taskService.addChild(root, sub);
//			
//			System.out.println(root);
//			System.out.println(sub);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//@Test
//	public void testAddTenSubTask(){
//		try {
//			NsTree sub;// = new TaskTree();
//			for(int i=0;i<10;i++){
//				NsTree root = taskService.findRoot("Admin");
//
//				sub = new NsTree();
//				sub.setName("Sub" + i);
//				sub = taskService.addChild(root, sub);
//			}
//			
//			testGetChildTask();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//@Test
//	public void testAddNearTask(){
//		try {
//			
//			List<NsTree> sub_tree_list = taskService.findByName("Sub020");
//			NsTree sub_tree0 = sub_tree_list.get(0);
//			NsTree new_tree0 = new NsTree(); 
//			new_tree0.setName("NearTree");
//			new_tree0 = taskService.addChildBefore(sub_tree0, new_tree0);
//			
//			System.out.println(new_tree0);
//			
//			testGetChildTask();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//@Test
//	public void testAddFiveDepthSubTask(){
//		try {
//			NsTree root = taskService.findRoot("Admin");
//			List<NsTree> sub_list = taskService.findChildren(root);
//			for(NsTree entity : sub_list){
//				List<NsTree> sub_tree_list = taskService.findByName(entity.getName());
//				NsTree add_tree = new NsTree();
//				add_tree.setName(entity.getName() + entity.getId());
//				NsTree sub_tree0 = sub_tree_list.get(0);
//
//				taskService.addChild(sub_tree0, add_tree);
//			}
//			
//			testGetChildTask();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//@Test
//	public List<NsTree> testGetChildTask(){
//		List<NsTree> list = null;
//		
//		try {
//			NsTree root = taskService.findRoot("Admin");
//			list = taskService.findSubTree(root);
//			
//			System.out.println(root);
//			for(NsTree tree : list){
//				String depth_str = "";
//				for(int i=0;i<tree.getDepth();i++){
//					depth_str +="-";
//				}
//				System.out.println(depth_str + tree);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return list;
//	}
//	
//	//@Test
//	public void testRemoveTask(){
//		testGetChildTask();
//		try {
//			NsTree del_task = taskService.find(45);
//			if(del_task != null){
//				taskService.remove(45);
//			}
//			testGetChildTask();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		testGetChildTask();
//	}
//	
//	//@Test
//	public void testMoveTaskBefore1(){
//		testGetChildTask();
//		List<NsTree> targets1 = taskService.findByName("Sub525");
//		List<NsTree> targets2 = taskService.findByName("Sub626");
//		try {
//			NsTree target	 = targets1.get(0);
//			NsTree before  = targets2.get(0);
//			
//			System.out.println("taget : " + target);
//			System.out.println("before: " + before);
//			
//			taskService.moveBefore(target, before);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		testGetChildTask();
//	}
//	
//	//@Test
//	public void testMoveTaskBefore2(){
//		testGetChildTask();
//		NsTree target = taskService.find(29);
//		NsTree before = taskService.find(34);
//		try {
//			System.out.println("taget : " + target);
//			System.out.println("before: " + before);
//			
//			taskService.moveBefore(target, before);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		testGetChildTask();
//	}
//	
//	//@Test
//	public void testMoveTaskAfter1(){
//		testGetChildTask();
//		NsTree target = taskService.find(38);
//		NsTree before = taskService.find(33);
//		try {
//			System.out.println("taget : " + target);
//			System.out.println("before: " + before);
//			
//			taskService.moveBefore(target, before);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		testGetChildTask();
//	}
//	//@Test
//	public void testMoveTask(){
//		testGetChildTask();
//		NsTree target = taskService.find(50);
//		NsTree parent = taskService.find(24);
//		try {
//			System.out.println("taget : " + target);
//			System.out.println("parent: " + parent);
//			
//			taskService.move(parent,target);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		testGetChildTask();
//	}
//	
//	//@Test
//	public void testGetRoots(){
//		try {
//			List<NsTree> root_list = taskService.findRoots();
//			for(NsTree node :root_list ){
//				System.out.println("root : " + node);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//@Test
//	public void testGetParent(){
//		NsTree target = taskService.find(50);
//		
//		NsTree parent = taskService.findParent(target);
//		List<NsTree> path_list = taskService.getPath(target);
//		
//		System.out.println("target : " + target);
//		System.out.println("parent : " + parent);
//		
//		for(NsTree node : path_list){
//			String depth_str = "";
//			for(int i = 0 ; i<node.getDepth(); i++){
//				depth_str += "-";
//			}
//			System.out.println(depth_str + node);
//		}
//	}
}
