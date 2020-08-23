import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class EvalVisitor extends ProyectoBaseVisitor<DefaultMutableTreeNode> {

	@Override public DefaultMutableTreeNode visitProgram(ProyectoParser.ProgramContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Program");
    ctx.declaration().forEach(child -> {
      node.add(visit(child));
    });
    return node; 
  }

  @Override public DefaultMutableTreeNode visitDeclaration(ProyectoParser.DeclarationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Declaration");
    if (ctx.structDeclaration() != null) {
      node.add(visit(ctx.structDeclaration()));
    } else if (ctx.varDeclaration() != null) {
      node.add(visit(ctx.varDeclaration()));
    } else if (ctx.methodDeclaration() != null) {
      node.add(visit(ctx.methodDeclaration()));
    };
    return node; 
  }

  @Override public DefaultMutableTreeNode visitCommonVarDeclaration(ProyectoParser.CommonVarDeclarationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("commonVarDeclaration");
    node.add(visit(ctx.varType()));
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitArrayVarDeclaration(ProyectoParser.ArrayVarDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("arrayVarDeclaration");
    visitChildren(ctx);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitStructDeclaration(ProyectoParser.StructDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("structDeclaration");
    visitChildren(ctx);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitVarType(ProyectoParser.VarTypeContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("varType");
    visitChildren(ctx);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitMethodDeclaration(ProyectoParser.MethodDeclarationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("methodDeclaration");
    
    visitChildren(ctx);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitMethodType(ProyectoParser.MethodTypeContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitCommonParameter(ProyectoParser.CommonParameterContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitArrayParameter(ProyectoParser.ArrayParameterContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitParameterType(ProyectoParser.ParameterTypeContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitBlock(ProyectoParser.BlockContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitIfStatement(ProyectoParser.IfStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitWhileStatement(ProyectoParser.WhileStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitReturnStatement(ProyectoParser.ReturnStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitMethodCallStatement(ProyectoParser.MethodCallStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitBlockStatement(ProyectoParser.BlockStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitLocationStatement(ProyectoParser.LocationStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionStatement(ProyectoParser.ExpressionStatementContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitLocation(ProyectoParser.LocationContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionLiteral(ProyectoParser.ExpressionLiteralContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionNegative(ProyectoParser.ExpressionNegativeContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionNot(ProyectoParser.ExpressionNotContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionLocation(ProyectoParser.ExpressionLocationContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionGroup(ProyectoParser.ExpressionGroupContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionMethodCall(ProyectoParser.ExpressionMethodCallContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitExpressionCommon(ProyectoParser.ExpressionCommonContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitMethodCall(ProyectoParser.MethodCallContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitOp(ProyectoParser.OpContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitArithOp(ProyectoParser.ArithOpContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitRelOp(ProyectoParser.RelOpContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitEqOp(ProyectoParser.EqOpContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitCondOp(ProyectoParser.CondOpContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitIntLiteral(ProyectoParser.IntLiteralContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitCharLiteral(ProyectoParser.CharLiteralContext ctx) { return visitChildren(ctx); }
  @Override public DefaultMutableTreeNode visitBoolLiteral(ProyectoParser.BoolLiteralContext ctx) { return visitChildren(ctx); }
}
