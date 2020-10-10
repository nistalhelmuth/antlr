import java.rmi.MarshalException;
import java.util.List;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class Node {
  public DefaultMutableTreeNode tree;
  //private Integer numChilds;
  //private Integer numTargets;
  //public String[] targets;
  //public Node[] childs;
  private Integer count;
  private List<Object> list;

  public Node(DefaultMutableTreeNode root){
    super();
    //this.numChilds = 0;    
    //this.numTargets = 0;    
    //this.childs = new Node[10];
    //this.targets = new String[10];
    count = 0;

    list = new ArrayList<Object>();

    this.tree = root;
  }

  public void add(Node child){
    // childs[numChilds] = child;
    // numChilds = numChilds + 1;
    list.add(child);
    count = count + 1;
    tree.add(child.tree);
  }
  
  public void addInstruction(String instruction){
    //targets[numTargets] = instruction;
    //numTargets = numTargets + 1;
    list.add(instruction);
    count = count + 1;
  }

  public void translate(){
    // for (int i = 0; i < numChilds; i++) {
    //   childs[i].translate();
    // }
    // for (int i = 0; i < numTargets; i++) {
    //   System.out.println(targets[i]);
    // }
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