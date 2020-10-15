import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;
import java.io.File;
import java.io.PrintStream;
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

    //File file = new File("logs.txt");
    //PrintStream stream = new PrintStream(file);
    //System.out.println("From now on "+file.getAbsolutePath()+" will have semantic errors");
    //System.setOut(stream);

    EvalVisitor eval = new EvalVisitor();
    //DefaultMutableTreeNode node = eval.visit(tree);
    Node root = eval.visit(tree);
    // root.translate();
    
    // JFrame frame = new JFrame("Demo");  
    // JTree jtree = new JTree(root.tree);
    // frame.add(jtree);
    // frame.setSize(550,800);
    // frame.setBounds(10,520, 500,300);  
    // frame.setVisible(true);
  }
}