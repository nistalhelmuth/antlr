import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;
import java.io.OutputStream;

public class Proyecto {
  public static class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
    
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
    
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked
   * from the event dispatch thread.
   */
  private static void createAndShowGUI() {
      // Create and set up the window.
      JFrame frame = new JFrame("TabbedPaneDemo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 800);

      final JTextArea codigoTA = new JTextArea();
      

      // file
      JMenuBar mb = new JMenuBar();
      JMenuItem m1 = new JMenu("FILE");
      JMenuItem m11 = new JMenuItem("OPEN");
      m1.add(m11);
      m11.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              JFileChooser fileChooser = new JFileChooser();
              fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
              int result = fileChooser.showOpenDialog(frame);
              if (result == JFileChooser.APPROVE_OPTION) {
                  File selectedFile = fileChooser.getSelectedFile();
                  codigoTA.setText("");
                  InputStream is = System.in;
                  String input = selectedFile.getAbsolutePath();
                  try {
                  if ( input != null ) is = new FileInputStream(input);
                  int i;
                  while ((i = is.read()) != -1) {
                      codigoTA.append(Character.toString((char) i));
                  }
                  } catch(Exception ex) {
                    System.out.println("ouch");
                  }
              }
          }
      });
      mb.add(m1);

      JTabbedPane tabbedPane = new JTabbedPane();


      JPanel panel = new JPanel(false);
      panel.setLayout(new GridLayout(1, 1));
      panel.add(codigoTA);
      tabbedPane.addTab("Codigo", panel);
      tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

      panel = new JPanel(false);
      final JTextArea erroresTA = new JTextArea();
      panel.setLayout(new GridLayout(1, 1));
      panel.add(erroresTA);
      tabbedPane.addTab("Errores", panel);
      tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

      JPanel treePanel = new JPanel(false);
      //final JTree jtree = new JTree();
      treePanel.setLayout(new GridLayout(1, 1));
      //treePanel.add(jtree);
      tabbedPane.addTab("Arbol", treePanel);
      tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

      panel = new JPanel(false);
      panel.setLayout(new GridLayout(1, 1));
      final JTextArea intermedioTA = new JTextArea();
      panel.add(intermedioTA);
      tabbedPane.addTab("Intermedio", panel);
      tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

      //panel = new JPanel(false);
      //ta = new JTextArea("Final");
      //panel.setLayout(new GridLayout(1, 1));
      //panel.add(ta);
      //tabbedPane.addTab("Final", panel);
      //tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

      // Add the tabbed pane to this panel.
      JPanel rootPanel = new JPanel();
      rootPanel.setLayout(new GridLayout(1, 1));
      rootPanel.add(tabbedPane);


      panel = new JPanel(); // the panel is not visible in output
      JButton compile = new JButton("Compile");
      compile.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String contents = codigoTA.getText();

              ANTLRInputStream input = new ANTLRInputStream(contents);
              ProyectoLexer lexer = new ProyectoLexer(input);
              CommonTokenStream tokens = new CommonTokenStream(lexer);
              ProyectoParser parser = new ProyectoParser(tokens);
              ParseTree tree = parser.program();

              //errores
              erroresTA.setText("");
              PrintStream printStream = new PrintStream(new CustomOutputStream(erroresTA));
              System.setOut(printStream);
              EvalVisitor eval = new EvalVisitor();
              Node root = eval.visit(tree);
              
              JTree jtree = new JTree(root.tree);
              treePanel.add(jtree);

              intermedioTA.setText("");
              printStream = new PrintStream(new CustomOutputStream(intermedioTA));
              System.setOut(printStream);
              root.translate();
          }
      });

      JButton reset = new JButton("Reset");
      reset.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              codigoTA.setText("");
          }
      });

      panel.add(reset);
      panel.add(compile);

      // Display the window.
      frame.getContentPane().add(BorderLayout.NORTH, mb);                
      frame.add(BorderLayout.CENTER, rootPanel);
      frame.getContentPane().add(BorderLayout.SOUTH, panel);
      frame.setVisible(true);
  }
  
  public static void main(String[] args) {
      //Schedule a job for the event dispatch thread:
      //creating and showing this application's GUI.
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
        //Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        createAndShowGUI();
        }
      });
  }
}
