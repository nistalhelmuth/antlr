import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class EvalVisitor extends ProyectoBaseVisitor<DefaultMutableTreeNode> {
  SimbolTable myTable = new SimbolTable();

	@Override public DefaultMutableTreeNode visitProgram(ProyectoParser.ProgramContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Program");
    //inicializar myTable
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
      System.out.println(ctx.toString());
      node.add(visit(ctx.varDeclaration()));
    } else if (ctx.methodDeclaration() != null) {
      node.add(visit(ctx.methodDeclaration()));
    };
    return node; 
  }

  @Override public DefaultMutableTreeNode visitCommonVarDeclaration(ProyectoParser.CommonVarDeclarationContext ctx) {
    if (ctx.varType().type != null) {
      //revsar que no sea void 
      System.out.println(ctx.varType().type.getText());
      System.out.println(ctx.ID().getText());
      myTable.putVariable(ctx.varType().type.getText(), ctx.ID().getText());
    } else { //handle structs

    }
    
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("commonVarDeclaration");
    node.add(visit(ctx.varType()));
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    
    return node; 
  }

  @Override public DefaultMutableTreeNode visitArrayVarDeclaration(ProyectoParser.ArrayVarDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("arrayVarDeclaration");
    visitChildren(ctx);
    try {
      //myTable.put(tipo = ctx.varType().text? ,id = ctx.ID().getText(), cantidad);
    }
    catch(Exception e) {
      //  Block of code to handle errors
    }
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
    node.add(visit(ctx.methodType()));
    ctx.parameter().forEach(child -> {
      node.add(visit(child));
    });
    node.add(visit(ctx.block()));
    return node; 
  }

  @Override public DefaultMutableTreeNode visitMethodType(ProyectoParser.MethodTypeContext ctx) {
    String name = "MethodType: " + ctx.type.getText();
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
    return node;
  }

  @Override public DefaultMutableTreeNode visitCommonParameter(ProyectoParser.CommonParameterContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("CommonParameter");
    node.add(visit(ctx.parameterType()));
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    return node;
  }

  @Override public DefaultMutableTreeNode visitArrayParameter(ProyectoParser.ArrayParameterContext ctx) {

    return visitChildren(ctx);
  }

  @Override public DefaultMutableTreeNode visitParameterType(ProyectoParser.ParameterTypeContext ctx) {
    String name = "ParameterType: " + ctx.type.getText();
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
    return node;
  }

  @Override public DefaultMutableTreeNode visitBlock(ProyectoParser.BlockContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Block");
    ctx.varDeclaration().forEach(child -> {
      node.add(visit(child));
    });
    ctx.statement().forEach(child -> {
      node.add(visit(child));
    });
    return node;
  }

  @Override public DefaultMutableTreeNode visitIfStatement(ProyectoParser.IfStatementContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("IfStatement");
    node.add(visit(ctx.expression()));
    node.add(visit(ctx.block(0)));
    if (ctx.block(1) != null) {
      node.add(visit(ctx.block(1)));
    }
    return node;
  }

  @Override public DefaultMutableTreeNode visitWhileStatement(ProyectoParser.WhileStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("WhileStatement");
    node.add(visit(ctx.expression()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitReturnStatement(ProyectoParser.ReturnStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ReturnStatement");
    if (ctx.expression() != null) {
      node.add(visit(ctx.expression()));
    };
    return node;
  }

  @Override public DefaultMutableTreeNode visitMethodCallStatement(ProyectoParser.MethodCallStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("MethodCallStatement");
    node.add(visit(ctx.methodCall()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitBlockStatement(ProyectoParser.BlockStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("BlockStatement");
    node.add(visit(ctx.block()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitLocationStatement(ProyectoParser.LocationStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("LocationStatement");
    node.add(visit(ctx.location()));
    node.add(visit(ctx.expression()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionStatement(ProyectoParser.ExpressionStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionStatement");
    // node.add(visit(ctx.expression()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitLocation(ProyectoParser.LocationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Location");
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    if(ctx.expression() != null) {
      node.add(visit(ctx.expression()));
    }
    if(ctx.location() != null) {
      node.add(visit(ctx.location()));
    }
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionLiteral(ProyectoParser.ExpressionLiteralContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionLiteral");
    node.add(visit(ctx.literal()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionNegative(ProyectoParser.ExpressionNegativeContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionNegative");
    node.add(visit(ctx.expression()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionNot(ProyectoParser.ExpressionNotContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionNot");
    node.add(visit(ctx.expression()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionLocation(ProyectoParser.ExpressionLocationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionLocation");
    node.add(visit(ctx.location()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionGroup(ProyectoParser.ExpressionGroupContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionGroup");
    node.add(visit(ctx.expression()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionMethodCall(ProyectoParser.ExpressionMethodCallContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionMethodCall");
    node.add(visit(ctx.methodCall()));
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionCommon(ProyectoParser.ExpressionCommonContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionCommon");
    node.add(visit(ctx.expression(0)));
    node.add(visit(ctx.op()));
    node.add(visit(ctx.expression(1)));
    return node;
  }

  @Override public DefaultMutableTreeNode visitMethodCall(ProyectoParser.MethodCallContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("MethodCall");
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    if (ctx.expression() != null) {
      ctx.expression().forEach(child -> {
        node.add(visit(child));
      });

    }
    return node;
  }

  @Override public DefaultMutableTreeNode visitOp(ProyectoParser.OpContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Op");
    // node.add(visit(ctx.simbol.getText()));
    return node;

  }

  @Override public DefaultMutableTreeNode visitArithOp(ProyectoParser.ArithOpContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ArithOp");
    // node.add(visit(ctx.simbol.getText()));
    return node;

  }

  @Override public DefaultMutableTreeNode visitRelOp(ProyectoParser.RelOpContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("RelOp");
    // node.add(visit(ctx.simbol.getText()));
    return node;

  }

  @Override public DefaultMutableTreeNode visitEqOp(ProyectoParser.EqOpContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("EqOp");
    // node.add(visit(ctx.simbol.getText()));
    return node;

  }

  @Override public DefaultMutableTreeNode visitCondOp(ProyectoParser.CondOpContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("CondOp");
    // node.add(visit(ctx.simbol.getText()));
    return node;

  }

  @Override public DefaultMutableTreeNode visitIntLiteral(ProyectoParser.IntLiteralContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("IntLiteral");
    // node.add(visit(ctx.simbol.getText()));
    return node;
  }
  
  @Override public DefaultMutableTreeNode visitCharLiteral(ProyectoParser.CharLiteralContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("CharLiteral");
    // node.add(visit(ctx.simbol.getText()));
    return node;
  }
  
  @Override public DefaultMutableTreeNode visitBoolLiteral(ProyectoParser.BoolLiteralContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("BoolLiteral");
    // node.add(visit(ctx.NUM.getText()));
    return node;
  }
  
}
