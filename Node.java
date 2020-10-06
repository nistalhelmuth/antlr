import java.rmi.MarshalException;

import javax.swing.tree.DefaultMutableTreeNode;

public class Node {
  public DefaultMutableTreeNode tree;
  private Integer numChilds;
  private String[] target;
  private Node[] childs;

  public Node(DefaultMutableTreeNode root){
    super();
    this.numChilds = 0;    
    this.childs = new Node[10];
    this.target = new String[10];
    this.tree = root;
  }

  public void add(Node child){
    childs[numChilds] = child;
    numChilds = numChilds + 1;
    tree.add(child.tree);
  }

  public void translate(){
      //por cada hijo
      //hijo.translate
      //print target
  }
}