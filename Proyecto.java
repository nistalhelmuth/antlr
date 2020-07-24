import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class Proyecto {
  public static void main(String args[]) throws Exception {        
    //new Test();  
    Path fileName = Path.of("test.txt");
    String actual = Files.readString(fileName);
    ANTLRInputStream input = new ANTLRInputStream(actual);
    ProyectoLexer lexer = new ProyectoLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ProyectoParser parser = new ProyectoParser(tokens);
    ParseTree tree = parser.program(); 
    //System.out.println(tree.toStringTree(parser));
    EvalVisitor eval = new EvalVisitor();

    DefaultMutableTreeNode node = eval.visit(tree);
    
    JFrame frame = new JFrame("Demo");  
    JTree jtree = new JTree(node);
    frame.add(jtree);
    frame.setSize(550,400);
    frame.setBounds(10,520, 500,300);  
    frame.setVisible(true);
  }
}