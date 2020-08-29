import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class Proyecto {
  public static void main(String args[]) throws Exception {        
    
    String inputFile = null;
    if ( args.length>0 ) inputFile = args[0];
    InputStream is = System.in;
    if ( inputFile!=null ) is = new FileInputStream(inputFile);
    ANTLRInputStream input = new ANTLRInputStream(is);

    ProyectoLexer lexer = new ProyectoLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ProyectoParser parser = new ProyectoParser(tokens);
    ParseTree tree = parser.program(); 
    // System.out.println(tree.toStringTree(parser));

    EvalVisitor eval = new EvalVisitor();
    DefaultMutableTreeNode node = eval.visit(tree);
    
    JFrame frame = new JFrame("Demo");  
    JTree jtree = new JTree(node);
    frame.add(jtree);
    frame.setSize(550,800);
    frame.setBounds(10,520, 500,300);  
    frame.setVisible(true);
  }
}