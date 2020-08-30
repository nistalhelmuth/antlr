import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class EvalVisitor extends ProyectoBaseVisitor<DefaultMutableTreeNode> {
  SimbolTable myTable = new SimbolTable();

	@Override public DefaultMutableTreeNode visitProgram(ProyectoParser.ProgramContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Program");
    ctx.declaration().forEach(child -> {
      node.add(visit(child));
    });
    myTable.show();
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

    if (ctx.varType().type != null) {
      myTable.putCommonVariable(ctx.varType().type.getText(), ctx.ID().getText());
    } else { //maneja structs
      if(ctx.varType().ID() != null) {
        myTable.putCommonVariable(ctx.varType().ID(), "struct", ctx.ID().getText());
      } else {
        myTable.putCommonVariable(ctx.varType().structDeclaration().ID(), "struct", ctx.ID().getText());
      }
    }  
    return node; 
  }

  @Override public DefaultMutableTreeNode visitArrayVarDeclaration(ProyectoParser.ArrayVarDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("arrayVarDeclaration");
    node.add(visit(ctx.varType()));
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    DefaultMutableTreeNode NUMNode = new DefaultMutableTreeNode(ctx.NUM().getText());
    node.add(NUMNode);

    if (ctx.varType().type != null) {
      //revisar que NUM sea int
      myTable.putArrayVariable(ctx.varType().type.getText(), ctx.ID().getText(), Integer.parseInt(ctx.NUM().getText()));
    } else {
      if(ctx.varType().ID() != null) {
        myTable.putArrayVariable(ctx.varType().ID().getText() ,"struct", ctx.ID().getText(), Integer.parseInt(ctx.NUM().getText()));
      } else {
        myTable.putArrayVariable(ctx.varType().structDeclaration().ID().getText() ,"struct", ctx.ID().getText(), Integer.parseInt(ctx.NUM().getText()));
      }
    }
    return node; 
  }

  @Override public DefaultMutableTreeNode visitStructDeclaration(ProyectoParser.StructDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("structDeclaration");
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    HashMap<String, String> variables = new HashMap<String, String>();
    ctx.varDeclaration().forEach(child -> {

      //variables comunes
      if (child.getClass() == ProyectoParser.CommonVarDeclarationContext.class) {
        ProyectoParser.CommonVarDeclarationContext childVariable = (ProyectoParser.CommonVarDeclarationContext) child;
        // tipos normales
        if (childVariable.varType().type != null) {
          variables.put(childVariable.varType().type.getText(), childVariable.ID().getText());
        } else { //Structs dentro de structs
          if(childVariable.varType().ID() != null) {
            variables.put(childVariable.varType().ID().getText(), childVariable.ID().getText());
          } else {
            variables.put(childVariable.varType().structDeclaration().ID().getText(), childVariable.ID().getText());
          }
        }

      } else {
        ProyectoParser.ArrayVarDeclarationContext childArrayVariable = (ProyectoParser.ArrayVarDeclarationContext) child;
        
      }
      node.add(visit(child));
    });

    myTable.putStructVariable(ctx.ID(), variables);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitVarType(ProyectoParser.VarTypeContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("varType");
    // falta el caso para evaluar structs
    visitChildren(ctx);
    return node; 
  }

  @Override public DefaultMutableTreeNode visitMethodDeclaration(ProyectoParser.MethodDeclarationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("methodDeclaration");

    HashMap<String, String> parameters = new HashMap<String, String>();
    node.add(visit(ctx.methodType()));
    ctx.parameter().forEach(child -> {
      if (child.getClass() == ProyectoParser.CommonParameterContext.class) {
        ProyectoParser.CommonParameterContext childParameter = (ProyectoParser.CommonParameterContext) child;
        parameters.put(childParameter.ID().getText(), childParameter.parameterType().type.getText());
      } else {
        ProyectoParser.ArrayParameterContext childArrayParameter = (ProyectoParser.ArrayParameterContext) child;
        parameters.put(childArrayParameter.ID().getText(), childArrayParameter.parameterType().type.getText()+'*');
      }
      node.add(visit(child));
    });
    myTable.putVariable(ctx.methodType().type.getText(), ctx.ID().getText(), parameters);

    myTable.createEnviroment(ctx.ID().getText());
    node.add(visit(ctx.block()));
    myTable.returnEnviroment();

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
