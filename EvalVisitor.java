import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class EvalVisitor extends ProyectoBaseVisitor<DefaultMutableTreeNode> {
  /**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitProgram(ProyectoParser.ProgramContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("PROGRAM");
    DefaultMutableTreeNode child =  visitChildren(ctx);
    node.add(child);
    return node; 
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitClassDeclaration(ProyectoParser.ClassDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    ctx.declaration().forEach(child -> {
      node.add(visit(child));
    });
    return node; 
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitDeclaration(ProyectoParser.DeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    DefaultMutableTreeNode child =  visitChildren(ctx);
    node.add(child);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitVarDeclaration(ProyectoParser.VarDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    //DefaultMutableTreeNode child =  visitChildren(ctx);
    //node.add(child);
    return node; 
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitStructDeclaration(ProyectoParser.StructDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    ctx.varDeclaration().forEach(child -> {
      node.add(visit(child));
    });
    return node; 
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitVarType(ProyectoParser.VarTypeContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    //DefaultMutableTreeNode child =  visitChildren(ctx);
    //node.add(child);
    return node; 
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitMethodDeclaration(ProyectoParser.MethodDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    // DefaultMutableTreeNode child =  visit(ctx.block());
    // node.add(child);
    return node; 
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitMethodType(ProyectoParser.MethodTypeContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitParameter(ProyectoParser.ParameterContext ctx) { 

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitParameterType(ProyectoParser.ParameterTypeContext ctx) { 

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitBlock(ProyectoParser.BlockContext ctx) { 

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitStatement(ProyectoParser.StatementContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitLocation(ProyectoParser.LocationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitExpression(ProyectoParser.ExpressionContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitMethodCall(ProyectoParser.MethodCallContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitOp(ProyectoParser.OpContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx.getText());
    visitChildren(ctx);
    return node;
  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public DefaultMutableTreeNode visitLiteral(ProyectoParser.LiteralContext ctx) { 
    String value = "";
    
    System.out.println(ctx.getText());
    if(ctx.NUM() != null) {
      value = ctx.NUM().getText();
    } else if (ctx.BOOL() != null) {
     value = ctx.BOOL().getText();
    } else if (ctx.CHAR() != null) {
      value = ctx.CHAR().getText();
    }
    System.out.println(value);
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(value);
    return node;
  }
  
}
