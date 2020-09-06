import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

import java.beans.Expression;
import java.util.*;

public class EvalVisitor extends ProyectoBaseVisitor<DefaultMutableTreeNode> {
  public SimbolTable myTable = new SimbolTable();

  private Pair<String, Integer> getLocationType(ProyectoParser.LocationContext ctx){
    if (ctx.location() != null) {
      return myTable.getStructVariable(ctx);
    }
    Data data = myTable.getVariable(ctx.ID().getText());
    if (data!=null) {
      return data.tipo;
    } 
    System.out.println(String.format("%s: location <%s> doesnt exists", ctx.start.getLine(), ctx.ID().getText()));
    return null;
  }

  private Pair<String, Integer> getExpressionType(ProyectoParser.ExpressionContext ctx){
    if (ctx.getClass() == ProyectoParser.ExpressionLocationContext.class){
      ProyectoParser.ExpressionLocationContext test = (ProyectoParser.ExpressionLocationContext) ctx;
      return getLocationType(test.location());
    } else if (ctx.getClass() == ProyectoParser.ExpressionMethodCallContext.class) {
      ProyectoParser.ExpressionMethodCallContext test = (ProyectoParser.ExpressionMethodCallContext) ctx;
      Data data = myTable.getVariable(test.methodCall().ID().getText());
      return data.tipo;
    } else if(ctx.getClass() == ProyectoParser.ExpressionLiteralContext.class){
      ProyectoParser.ExpressionLiteralContext test = (ProyectoParser.ExpressionLiteralContext) ctx;
      //System.out.println(test.literal().getClass());
      if (test.literal().getClass() == ProyectoParser.BoolLiteralContext.class) {
        return new Pair<String, Integer>("boolean");
      } else if(test.literal().getClass() == ProyectoParser.CharLiteralContext.class) {
        return new Pair<String, Integer>("char");
      } else if(test.literal().getClass() == ProyectoParser.IntLiteralContext.class) {
        return new Pair<String, Integer>("int");
      }
    } else if(ctx.getClass() == ProyectoParser.ExpressionCommonContext.class) {
      ProyectoParser.ExpressionCommonContext test = (ProyectoParser.ExpressionCommonContext) ctx;


      Pair<String, Integer> tipo0 = getExpressionType(test.expression(0));
      Pair<String, Integer> tipo1 = getExpressionType(test.expression(1));
      if(test.op().arithOp() != null) {
        if(tipo0.equals(new Pair<String, Integer>("int")) && tipo0.equals(tipo1)) {
          return new Pair<String, Integer>("int");
        } else {
          System.out.println(String.format("Check types of %s: expression must be integer", test.start.getLine()));
        }
      } else if(test.op().relOp() != null) {
        
        if(tipo0.equals(new Pair<String, Integer>("int")) && tipo0.equals(tipo1)) {
          return new Pair<String, Integer>("boolean");
        } else {
          System.out.println(String.format("Check types of %s: expression must be integer", test.start.getLine()));
        }
      } else if(test.op().eqOp() != null) {
        if(tipo0.equals(tipo1)) {
          return new Pair<String, Integer>("boolean");
        } else {
          System.out.println(String.format("Check types of %s: expression must be equal", test.start.getLine()));
        }
      } else if(test.op().condOp() != null) {
        if(tipo0.equals(new Pair<String, Integer>("boolean")) && tipo0.equals(tipo1)) {
          return new Pair<String, Integer>("boolean");
        } else {
          System.out.println(String.format("Check types of %s: expression must be boolean", test.start.getLine()));
        }
      } 
    } else if (ctx.getClass() == ProyectoParser.ExpressionNegativeContext.class) {
      ProyectoParser.ExpressionNegativeContext test = (ProyectoParser.ExpressionNegativeContext) ctx;
      Pair type = getExpressionType(test.expression());
      if (type.equals(new Pair<String, Integer>("int"))){
        return type;
      }
      System.out.println(String.format("%s: The expressions isnt integer", ctx.start.getLine()));
    } else if (ctx.getClass() == ProyectoParser.ExpressionNotContext.class) {
      ProyectoParser.ExpressionNegativeContext test = (ProyectoParser.ExpressionNegativeContext) ctx;
      Pair type = getExpressionType(test.expression());
      if (type.equals(new Pair<String, Integer>("bool"))){
        return type;
      }
      System.out.println(String.format("%s: The expressions isnt boolean", ctx.start.getLine()));
    } else if (ctx.getClass() == ProyectoParser.ExpressionGroupContext.class) {
      ProyectoParser.ExpressionGroupContext test = (ProyectoParser.ExpressionGroupContext) ctx;
      return getExpressionType(test.expression());
    }

    return null;
  }

	@Override public DefaultMutableTreeNode visitProgram(ProyectoParser.ProgramContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Program");
    ctx.declaration().forEach(child -> {
      node.add(visit(child));
    });

    Data main = myTable.isMainCreated();
    if (main == null || main.parametros == null){
      System.out.println(String.format("%s: Main is invalid or doesnt exits" ,ctx.start.getLine()));
    }
    //myTable.show();

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
        myTable.putCommonVariable(ctx.varType().ID().getText(), "struct", ctx.ID().getText());
      } else {
        myTable.putCommonVariable(ctx.varType().structDeclaration().ID().getText(), "struct", ctx.ID().getText());
      }
    }  
    return node; 
  }

  @Override public DefaultMutableTreeNode visitArrayVarDeclaration(ProyectoParser.ArrayVarDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("arrayVarDeclaration");
    node.add(visit(ctx.varType()));
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    Integer numValue;
    try {
      DefaultMutableTreeNode NUMNode = new DefaultMutableTreeNode(ctx.NUM().getText());
      node.add(NUMNode);
      numValue = Integer.parseInt(ctx.NUM().getText());
   } catch(Exception ex) {
      System.out.println(String.format("%s: Unavalible input for array dimention on <%s>", ctx.start.getLine(), ctx.ID().getText()));
      numValue = 0;
   }

    if (numValue < 0) {
      System.out.println(String.format("%s: Unavalible size for string %s", ctx.start.getLine(), ctx.ID().getText()));
    } else {
      if (ctx.varType().type != null) {
        //revisar que NUM sea int
        myTable.putArrayVariable(ctx.varType().type.getText(), ctx.ID().getText(), numValue);
      } else {
        if(ctx.varType().ID() != null) {
          myTable.putArrayVariable(ctx.varType().ID().getText() ,"struct", ctx.ID().getText(), numValue);
        } else {
          myTable.putArrayVariable(ctx.varType().structDeclaration().ID().getText() ,"struct", ctx.ID().getText(), numValue);
        }
      }
    }

    
    return node; 
  }

  @Override public DefaultMutableTreeNode visitStructDeclaration(ProyectoParser.StructDeclarationContext ctx) { 
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("structDeclaration");
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    LinkedHashMap<String, Pair<String, Integer>> variables = new LinkedHashMap<String, Pair<String, Integer>>();
    ctx.varDeclaration().forEach(child -> {
      // para que no se declaren los hijos
      // node.add(visit(child));

      //variables comunes
      if (child.getClass() == ProyectoParser.CommonVarDeclarationContext.class) {
        ProyectoParser.CommonVarDeclarationContext childVariable = (ProyectoParser.CommonVarDeclarationContext) child;
        // tipos normales
        if (childVariable.varType().type != null) {
          Pair pair = new Pair<String, Integer>(childVariable.varType().type.getText());
          variables.put(childVariable.ID().getText(), pair);
        } else { //Structs dentro de structs
          if(childVariable.varType().ID() != null) {
            Pair pair = new Pair<String, Integer>(childVariable.varType().ID().getText());
            variables.put(childVariable.ID().getText(), pair);
          } else {
            Pair pair = new Pair<String, Integer>(childVariable.varType().structDeclaration().ID().getText());
            variables.put(childVariable.ID().getText(), pair);
          }
        }

      } else { //varialbes array
        ProyectoParser.ArrayVarDeclarationContext childArrayVariable = (ProyectoParser.ArrayVarDeclarationContext) child;
        // tipos normales
        if (childArrayVariable.varType().type != null) {
          Pair pair = new Pair<String, Integer>(childArrayVariable.varType().type.getText(), Integer.parseInt(childArrayVariable.NUM().getText()));
          variables.put(childArrayVariable.ID().getText(), pair);
        } else { //Structs dentro de structs
          if(childArrayVariable.varType().ID() != null) {
            Pair pair = new Pair<String, Integer>(childArrayVariable.varType().ID().getText(), Integer.parseInt(childArrayVariable.NUM().getText()));
            variables.put(childArrayVariable.ID().getText(), pair);
          } else {
            Pair pair = new Pair<String, Integer>(childArrayVariable.varType().structDeclaration().ID().getText(), Integer.parseInt(childArrayVariable.NUM().getText()));
            variables.put(childArrayVariable.ID().getText(), pair);
          }
        }
      }
    });

    myTable.putStructVariable(ctx.ID().getText(), variables);
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

    LinkedHashMap<String, Pair<String, Integer>> parameters = new LinkedHashMap<String, Pair<String, Integer>>();
    node.add(visit(ctx.methodType()));

    ctx.parameter().forEach(child -> {
      if (child.getClass() == ProyectoParser.CommonParameterContext.class) {
        ProyectoParser.CommonParameterContext childParameter = (ProyectoParser.CommonParameterContext) child;
        Pair pair = new Pair<String, Integer>(childParameter.parameterType().type.getText());
        parameters.put(childParameter.ID().getText(), pair);
      } else {
        ProyectoParser.ArrayParameterContext childArrayParameter = (ProyectoParser.ArrayParameterContext) child;
        Pair pair = new Pair<String, Integer>(childArrayParameter.parameterType().type.getText(), 0);
        parameters.put(childArrayParameter.ID().getText(), pair);
      }
      node.add(visit(child));
    });
    myTable.putMethodVariable(ctx.methodType().type.getText(), ctx.ID().getText(), parameters);

    myTable.createEnviroment(ctx.ID().getText());
    //add params as local variables

    for (String id: parameters.keySet()) {
      Pair<String, Integer> tipo = parameters.get(id);
      if (tipo.getSecond() != null){
        myTable.putArrayVariable(tipo.getFirst(), id, tipo.getSecond());
      } else {
        myTable.putCommonVariable(tipo.getFirst(), id);
      }
    }

    Boolean flag = false;
    if (!ctx.methodType().type.getText().equals("void") && ctx.block().statement().size() == 0) {
      flag = true;
    }
    for (ProyectoParser.StatementContext statement : ctx.block().statement()) {
      if(statement.getClass() == ProyectoParser.ReturnStatementContext.class) {
        ProyectoParser.ReturnStatementContext returnExpr = (ProyectoParser.ReturnStatementContext) statement;
        if (returnExpr.expression() == null && !ctx.methodType().type.getText().equals("void")) {
          flag = true;
        } else {
          try {
            Pair<String, Integer> type = getExpressionType(returnExpr.expression());
            if(
              (type.equals(new Pair<String, Integer>("int")) && !ctx.methodType().type.getText().equals("int"))
              || (type.equals(new Pair<String, Integer>("char")) && !ctx.methodType().type.getText().equals("char"))
              || (type.equals(new Pair<String, Integer>("boolean")) && !ctx.methodType().type.getText().equals("boolean"))
            ){
              flag = true;
            }
          } catch (NullPointerException e) {
            System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
          }
        }
      }
    }
    if(flag){
      System.out.println(String.format("%s: Check return type on <%s>", ctx.start.getLine(), ctx.ID().getText()));
    }
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

    try {
      if (!getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("boolean"))){
        System.out.println(String.format("%s: expression on if has to be boolean", ctx.start.getLine()));
      }
    } catch (NullPointerException e) {
      System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
    }
    
    


    node.add(visit(ctx.block(0)));
    if (ctx.block(1) != null) {
      node.add(visit(ctx.block(1)));
    }
    return node;
  }

  @Override public DefaultMutableTreeNode visitWhileStatement(ProyectoParser.WhileStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("WhileStatement");
    if (!getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("boolean"))){
      System.out.println(String.format("%s: expression on if has to be boolean", ctx.start.getLine()));
    }
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
    //node.add(visit(ctx.location()));
    node.add(visit(ctx.expression()));

    try {
      Pair<String, Integer> tipoLocation = getLocationType(ctx.location());
      Pair<String, Integer> tipoExpression = getExpressionType(ctx.expression());
      if (tipoLocation == null) {
          System.out.println(String.format("%s: location doenst exists", ctx.start.getLine()));
      } else if (tipoExpression == null) {
        System.out.println(String.format("%s: expression doenst exists", ctx.start.getLine()));
      }else if(!tipoLocation.getFirst().equals(tipoExpression.getFirst())){
      //if(!tipoLocation.equals(tipoExpression)){
        System.out.println(String.format("%s: Check types of location and expression", ctx.start.getLine()));
      }
    } catch (NullPointerException e) {
        System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
    }
    
    
    
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionStatement(ProyectoParser.ExpressionStatementContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionStatement");
    if (ctx.expression() != null){
      node.add(visit(ctx.expression()));
    }
    return node;
  }

  @Override public DefaultMutableTreeNode visitLocation(ProyectoParser.LocationContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Location");
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);
    if (ctx.expression() != null) {
      if (getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("Integer"))) {
        node.add(visit(ctx.expression()));
      } else {
        System.out.println(String.format("%s: NUM isnt integer", ctx.start.getLine()));
      }
    }
    if(ctx.location() != null) {
      node.add(visit(ctx.location()));
    }

    // Data data = myTable.getVariable(ctx.ID().getText());
    // if (data == null) {
    //   System.out.println(String.format("%s: location <%s> hasn't been declarated", ctx.start.getLine(), ctx.ID().getText()));
    // }

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
    if(getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("int"))) {
      System.out.println(String.format("%s: Check type of expression must be integer", ctx.start.getLine()));
    }
    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionNot(ProyectoParser.ExpressionNotContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionNot");
    node.add(visit(ctx.expression()));
    if(getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("boolean"))) {
      System.out.println(String.format("%s: Check type of expression must be integer", ctx.start.getLine()));
    }
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

    Data method = myTable.getVariable(ctx.methodCall().ID().getText()); 
    if(method.tipo.equals(new Pair<String, Integer>("void"))){
      System.out.println(String.format("%s: Expected return value on <%s>", ctx.start.getLine(), method.id));
    }

    return node;
  }

  @Override public DefaultMutableTreeNode visitExpressionCommon(ProyectoParser.ExpressionCommonContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("ExpressionCommon");
    // revisar que las expressiones concuerden con el tipo de operador

    try {
      Pair<String, Integer> tipo0 = getExpressionType(ctx.expression(0));
      Pair<String, Integer> tipo1 = getExpressionType(ctx.expression(1));
      if(ctx.op().arithOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("int")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be integer", ctx.start.getLine()));
        }
      } else if(ctx.op().relOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("int")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be integer", ctx.start.getLine()));
        }
      } else if(ctx.op().eqOp() != null) {
        if(!tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be equal", ctx.start.getLine()));
        }
      } else if(ctx.op().condOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("boolean")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be boolean", ctx.start.getLine()));
        }
      } else {
        System.out.println(String.format("%s: unavalible op on expression", ctx.start.getLine()));
      }
    } catch (NullPointerException e) {
        System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
    }

    

    node.add(visit(ctx.expression(0)));
    node.add(visit(ctx.op()));
    node.add(visit(ctx.expression(1)));
    return node;
  }

  @Override public DefaultMutableTreeNode visitMethodCall(ProyectoParser.MethodCallContext ctx) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("MethodCall");
    DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    node.add(IDNode);

    Data methodDefinition = myTable.getVariable(ctx.ID().getText());
    if (methodDefinition == null) {
      System.out.println(String.format("%s: method <%s> hasn't been declarated", ctx.start.getLine(), ctx.ID().getText()));
    }
    if (ctx.expression() != null) {
      Integer i = 0;
      for ( ProyectoParser.ExpressionContext child : ctx.expression()){
        node.add(visit(child));

        Pair<String, Integer> expressionType = getExpressionType(child);
        if (i < methodDefinition.parametros.values().toArray().length){
          Pair<String, Integer> param = (Pair<String,Integer>) methodDefinition.parametros.values().toArray()[i];
          if(param != null && !param.equals(expressionType)){
            System.out.println(String.format("%s: check param <%s> on method <%s>", 
              ctx.start.getLine(),
              expressionType, 
              methodDefinition.id
            ));
          }
        } else {
          System.out.println(String.format("%s: more params than needed on method <%s>", ctx.start.getLine(), methodDefinition.id)); 
        }
        
        i = i+1;
      }
      /** 
      ctx.expression().forEach((child) -> {
        Pair<String, Integer> expressionType = getExpressionType(child);
        node.add(visit(child));

        Pair<String, Integer> param = (Pair<String,Integer>) methodDefinition.parametros.values().toArray()[0];
        if(!param.equals(expressionType)){
          System.out.println(String.format("check param <%s> on method <%s>", 
            expressionType, 
            methodDefinition.id
          ));
        }
        i = i+1;
      });
      */
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
