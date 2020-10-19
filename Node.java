import java.util.List;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class Node {
  public DefaultMutableTreeNode tree;
  private Integer count;
  private List<Object> list;

  public Node(DefaultMutableTreeNode root){
    super();
    count = 0;
    list = new ArrayList<Object>();
    this.tree = root;
  }

  public void add(Node child){
    list.add(child);
    count = count + 1;
    tree.add(child.tree);
  }
  
  public void addInstruction(String instruction){
    list.add(instruction);
    count = count + 1;
  }

  public void translate(){
    for( Object item: list ) {
			if( item instanceof Node) {
        Node child = (Node) item; 
				child.translate();
			} else if( item instanceof String) {
				System.out.println(item);
			} 			
		}
  }
}