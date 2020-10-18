import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*; 


public class EvalVisitor extends ProyectoBaseVisitor<Node> {
  public SimbolTable myTable = new SimbolTable();
  public ARMArquitecture myARM = new ARMArquitecture();

  private Pair<String, Integer> getLocationType(ProyectoParser.LocationContext ctx){
    Data data = null;
    if (ctx.location() != null) {
      while(ctx.location() != null){
        if (data != null) {
          data = data.dependencia;
        } else {
          data = myTable.getVariable(ctx.ID().getText());
        }
        if (data.variables.containsKey(ctx.location().ID().getText())) {
          data = data.variables.get(ctx.location().ID().getText());
        } 
        ctx = ctx.location();
      }
    } else {
      data = myTable.getVariable(ctx.ID().getText());
    }
    if (data != null) {
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
      if(test.op().arithOp() != null || test.op().highArithOp() != null) {
        if(tipo0.getFirst().equals(new Pair<String, Integer>("int").getFirst()) && tipo0.getFirst().equals(tipo1.getFirst())) {
          return new Pair<String, Integer>("int");
        } else {
          System.out.println(String.format("Check types of %s: expression must be integer", test.start.getLine()));
        }
      } else if(test.op().relOp() != null) {
        if(tipo0.getFirst().equals(new Pair<String, Integer>("int").getFirst()) && tipo0.getFirst().equals(tipo1.getFirst())) {
          return new Pair<String, Integer>("boolean");
        } else {
          System.out.println(String.format("Check types of %s: expression must be integer", test.start.getLine()));
        }
      } else if(test.op().eqOp() != null) {
        if(tipo0.getFirst().equals(tipo1.getFirst())) {
          return new Pair<String, Integer>("boolean");
        } else {
          System.out.println(String.format("Check types of %s: expression must be equal", test.start.getLine()));
        }
      } else if(test.op().condOp() != null) {
        if(tipo0.getFirst().equals(new Pair<String, Integer>("boolean").getFirst()) && tipo0.getFirst().equals(tipo1.getFirst())) {
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
      if (type.equals(new Pair<String, Integer>("boolean"))){
        return type;
      }
      System.out.println(String.format("%s: The expressions isnt boolean", ctx.start.getLine()));
    } else if (ctx.getClass() == ProyectoParser.ExpressionGroupContext.class) {
      ProyectoParser.ExpressionGroupContext test = (ProyectoParser.ExpressionGroupContext) ctx;
      return getExpressionType(test.expression());
    }
    return null;
  }

  public Node shortCircuitExpression(Node node, ProyectoParser.ExpressionContext ctx, Integer firstLabel, Integer secondLabel) {
    if (ctx.getClass() != ProyectoParser.ExpressionCommonContext.class){
      node.add(visit(ctx));
      node.addInstruction(String.format("IF T%d goto L%d", myARM.getLastRegister(), firstLabel));
    } else {
      ProyectoParser.ExpressionCommonContext expresionCommon = (ProyectoParser.ExpressionCommonContext) ctx;
      if (expresionCommon.op().condOp() != null && expresionCommon.op().condOp().simbol.getText().equals("&&")) {
        node.add(visit(expresionCommon.expression(0)));
        node.addInstruction(String.format("IFNOT T%d goto L%d", myARM.getLastRegister(), secondLabel));
        node.add(visit(expresionCommon.expression(1)));
        node.addInstruction(String.format("IFNOT T%d goto L%d", myARM.getLastRegister(), secondLabel));
        //node = shortCircuitExpression(node, expresionCommon.expression(1), firstLabel, secondLabel);

      } else if (expresionCommon.op().condOp() != null && expresionCommon.op().condOp().simbol.getText().equals("||")) {
        node.add(visit(expresionCommon.expression(0)));
        node.addInstruction(String.format("IF T%d goto L%d", myARM.getLastRegister(), firstLabel));
        node = shortCircuitExpression(node, expresionCommon.expression(1), firstLabel, secondLabel);
      } else {
        node.add(visit(ctx));
        node.addInstruction(String.format("IF T%d goto L%d", myARM.getLastRegister(), firstLabel));
      }
    }
    
    return node;
  }

  // Abstractions

	@Override public Node visitProgram(ProyectoParser.ProgramContext ctx) { 
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Program");
    Node node = new Node(treeNode);
    ctx.declaration().forEach(child -> {
      node.add(visit(child));
    });

    Data main = myTable.isMainCreated();
    if (main == null || main.parametros == null){
      System.out.println(String.format("%s: Main is invalid or doesnt exits" ,ctx.start.getLine()));
    }
    // myTable.show();

    return node; 
  }

  @Override public Node visitDeclaration(ProyectoParser.DeclarationContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Declaration");
    Node node = new Node(treeNode);
    if (ctx.structDeclaration() != null) {
      node.add(visit(ctx.structDeclaration()));

    } else if (ctx.varDeclaration() != null) {
      node.add(visit(ctx.varDeclaration()));

    } else if (ctx.methodDeclaration() != null) {
      node.add(visit(ctx.methodDeclaration()));
    };
    return node; 
  }

  @Override public Node visitCommonVarDeclaration(ProyectoParser.CommonVarDeclarationContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("commonVarDeclaration");
    Node node = new Node(treeNode);
    node.add(visit(ctx.varType()));

    Pair<String, Integer> id;
    String defaultValue = "";
    if (ctx.varType().type != null) {
      id = myTable.putCommonVariable(ctx.varType().type.getText(), ctx.ID().getText());
      String type = ctx.varType().type.getText();
      if(type.equals("int")){
        defaultValue = "0";
      } else if (type.equals("char")){
        defaultValue = "";
      } else if (type.equals("boolean")) {
        defaultValue = "true";
      }
    } else {
      // maneja structs
      if(ctx.varType().ID() != null) {
        id = myTable.putCommonVariable(ctx.varType().ID().getText(), "struct", ctx.ID().getText());
      } else {
        id = myTable.putCommonVariable(ctx.varType().structDeclaration().ID().getText(), "struct", ctx.ID().getText());
      }
      defaultValue = "<struct>";
    }
    node.addInstruction(String.format("%s[%d] = %s", id.getFirst(), id.getSecond(), defaultValue));
    return node; 
  }

  @Override public Node visitArrayVarDeclaration(ProyectoParser.ArrayVarDeclarationContext ctx) { 
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("arrayVarDeclaration");
    Node node = new Node(treeNode);
    node.add(visit(ctx.varType()));
    Integer numValue;
    try {
      numValue = Integer.parseInt(ctx.NUM().getText());
    } catch(Exception ex) {
      System.out.println(String.format("%s: Unavalible input for array dimention on <%s>", ctx.start.getLine(), ctx.ID().getText()));
      numValue = 0;
    }
    if (numValue < 0) {
      System.out.println(String.format("%s: Unavalible size for string %s", ctx.start.getLine(), ctx.ID().getText()));
    } else {
      if (ctx.varType().type != null) {
        // revisar que NUM sea int
        Pair<String, Integer> id = myTable.putArrayVariable(ctx.varType().type.getText(), ctx.ID().getText(), numValue);
        // agregar asignación a cada particion?
        String type = ctx.varType().type.getText();
        String defaultValue = "";
        if(type.equals("int")){
          defaultValue = "0";
        } else if (type.equals("char")){
          defaultValue = "";
        } else if (type.equals("boolean")) {
          defaultValue = "true";
        }
        node.addInstruction(String.format("%s[%d] = array<%s, %s>", id.getFirst(), id.getSecond(), numValue, defaultValue));
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

  @Override public Node visitStructDeclaration(ProyectoParser.StructDeclarationContext ctx) { 
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("structDeclaration");
    Node node = new Node(treeNode);

    List<Data> variables = new ArrayList<Data>();
    Integer localOffset = 0;
    for (ProyectoParser.VarDeclarationContext child : ctx.varDeclaration()) {
      // node.add(visit(child));
      //variables comunes
      Data variable;
      if (child.getClass() == ProyectoParser.CommonVarDeclarationContext.class) {
        ProyectoParser.CommonVarDeclarationContext childVariable = (ProyectoParser.CommonVarDeclarationContext) child;
        // tipos normales
        if (childVariable.varType().type != null) {
          variable = new Data(
            childVariable.ID().getText(),
            childVariable.varType().type.getText(),
            localOffset
          );
        } else { 
          //Structs dentro de structs
          if(childVariable.varType().ID() != null) {
            variable = new Data(
              myTable.getVariable(childVariable.varType().ID().getText()),
              childVariable.ID().getText(),
              "struct",
              localOffset
            );
          } else {
            variable = new Data(
              myTable.getVariable(childVariable.varType().structDeclaration().ID().getText()),
              childVariable.ID().getText(),
              "struct",
              localOffset
            );
          }
        }
       //varialbes array
      } else {
        ProyectoParser.ArrayVarDeclarationContext childArrayVariable = (ProyectoParser.ArrayVarDeclarationContext) child;
        // tipos normales
        if (childArrayVariable.varType().type != null) {
          variable = new Data(
            childArrayVariable.ID().getText(),
            childArrayVariable.varType().type.getText(),
            Integer.parseInt(childArrayVariable.NUM().getText()),
            localOffset
          );
        } else { //Structs dentro de structs
          if(childArrayVariable.varType().ID() != null) {
            variable = new Data(
              myTable.getVariable(childArrayVariable.varType().ID().getText()),
              childArrayVariable.ID().getText(),
              "struct",
              Integer.parseInt(childArrayVariable.NUM().getText()),
              localOffset
            );
          } else {
            variable = new Data(
              myTable.getVariable(childArrayVariable.varType().structDeclaration().ID().getText()),
              childArrayVariable.ID().getText(),
              "struct",
              Integer.parseInt(childArrayVariable.NUM().getText()),
              localOffset
            );
          }
        }
      }
      localOffset = localOffset + variable.size;
      variables.add(variable);
    };
    Pair<String, Integer> id = myTable.putStructVariable(ctx.ID().getText(), variables);
    node.addInstruction(String.format("%s[%d] = <struct>", id.getFirst(), id.getSecond()));
    return node; 
  }

  @Override public Node visitVarType(ProyectoParser.VarTypeContext ctx) { 
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("varType");
    Node node = new Node(treeNode);

    if (ctx.structDeclaration() != null) {
      node.add(visit(ctx.structDeclaration()));
    }

    return node; 
  }

  @Override public Node visitMethodDeclaration(ProyectoParser.MethodDeclarationContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("methodDeclaration");
    Node node = new Node(treeNode);
    node.add(visit(ctx.methodType()));
    node.addInstruction(String.format("\n\t%s:", ctx.ID().getText()));
    // node.addInstruction("push {r11, lr}");
    // node.addInstruction("add r11, sp, #0");
    // node.addInstruction("sub sp, sp, #16");
    node.addInstruction("//prologue");

    LinkedHashMap<String, Pair<String, Integer>> parameters = new LinkedHashMap<String, Pair<String, Integer>>();
    ctx.parameter().forEach(child -> {
      if (child.getClass() == ProyectoParser.CommonParameterContext.class) {
        ProyectoParser.CommonParameterContext childParameter = (ProyectoParser.CommonParameterContext) child;
        Pair pair = new Pair<String, Integer>(childParameter.parameterType().type.getText());
        parameters.put(childParameter.ID().getText(), pair);
        // node.addInstruction(String.format("POP T%s", myARM.getParam()));
      } else {
        ProyectoParser.ArrayParameterContext childArrayParameter = (ProyectoParser.ArrayParameterContext) child;
        Pair pair = new Pair<String, Integer>(childArrayParameter.parameterType().type.getText(), 0);
        parameters.put(childArrayParameter.ID().getText(), pair);
        // node.addInstruction(String.format("POP T%s", myARM.getParam()));
      }
      node.add(visit(child));
    });
    myARM.freeParams();
    myTable.putMethodVariable(ctx.methodType().type.getText(), ctx.ID().getText(), parameters);

    myTable.createEnviroment(ctx.ID().getText());

    //add params as local variables
    for (String id: parameters.keySet()) {
      Pair<String, Integer> tipo = parameters.get(id);
      Pair<String, Integer> pair;
      if (tipo.getSecond() != null){
        pair = myTable.putArrayVariable(tipo.getFirst(), id, tipo.getSecond());
        node.addInstruction(String.format("POP T%s", myARM.getParam()));
      } else {
        pair = myTable.putCommonVariable(tipo.getFirst(), id);
        // mover parametro a variable
      }
      node.addInstruction(String.format("POP %s[%d] T%s", pair.getFirst(), pair.getSecond(), myARM.getParam()));
    }
    myARM.freeParams();

    node.add(visit(ctx.block()));
    node.addInstruction("//epilogue");
    //node.addInstruction("sub sp, r11, #0");
    //node.addInstruction("pop {r11, pc} ");

    Boolean flag = false;
    // Boolean leafMethod = false;
    if (!ctx.methodType().type.getText().equals("void") && ctx.block().statement().size() == 0) {
      flag = true;
    }
    for (ProyectoParser.StatementContext statement : ctx.block().statement()) {
      if(statement.getClass() == ProyectoParser.ReturnStatementContext.class) {
        ProyectoParser.ReturnStatementContext returnExpr = (ProyectoParser.ReturnStatementContext) statement;
        if (returnExpr.expression() != null && ctx.methodType().type.getText().equals("void")) {
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
    } else {

    }
    

    myTable.returnEnviroment();
    return node; 
  }

  @Override public Node visitMethodType(ProyectoParser.MethodTypeContext ctx) {
    // String name = "MethodType: " + ctx.type.getText();
    // DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("MethodType");
    Node node = new Node(treeNode);

    return node;
  }

  @Override public Node visitCommonParameter(ProyectoParser.CommonParameterContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("CommonParameter");
    Node node = new Node(treeNode);
    node.add(visit(ctx.parameterType()));
    // DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    // node.add(IDNode);
    return node;
  }

  @Override public Node visitArrayParameter(ProyectoParser.ArrayParameterContext ctx) {
    return visitChildren(ctx);
  }

  @Override public Node visitParameterType(ProyectoParser.ParameterTypeContext ctx) {
    // String name = "ParameterType: " + ctx.type.getText();
    // DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ParameterType");
    Node node = new Node(treeNode);
    return node;
  }

  @Override public Node visitBlock(ProyectoParser.BlockContext ctx) {
    //DefaultMutableTreeNode node = new DefaultMutableTreeNode("Block");
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("visitBlock");
    Node node = new Node(treeNode);
    ctx.varDeclaration().forEach(child -> {
      node.add(visit(child));
    });
    ctx.statement().forEach(child -> {
      node.add(visit(child));
    });
    return node;
  }

  @Override public Node visitIfStatement(ProyectoParser.IfStatementContext ctx) { 
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("IfStatement");
    Node node = new Node(treeNode);
    try {
      if (!getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("boolean"))){
        System.out.println(String.format("%s: expression on if has to be boolean", ctx.start.getLine()));
      } else {
        Integer label1 = myARM.createLabel();
        Integer label2 = myARM.createLabel();
        Integer label3 = myARM.createLabel();
        node = shortCircuitExpression(node, ctx.expression(), label1, label2);
        node.addInstruction(String.format("goto L%d", label2));
        myARM.useLabel();
        node.addInstruction(String.format("L%d:", label1));
        myTable.createEnviroment(String.format("%s-IF", myTable.peekEnviroment()));
        node.add(visit(ctx.block(0)));
        myTable.returnEnviroment();
        myARM.useLabel();
        node.addInstruction(String.format("goto L%d", label3));
        node.addInstruction(String.format("L%d:", label2));
        myARM.useLabel();
        if (ctx.block(1) != null) {
          myTable.createEnviroment(String.format("%s-ELSE", myTable.peekEnviroment()));
          node.add(visit(ctx.block(1)));
          myTable.returnEnviroment();
        } 
        node.addInstruction(String.format("L%d:", label3));
      }
    } catch (NullPointerException e) {
      System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
    }
    return node;
  }

  @Override public Node visitWhileStatement(ProyectoParser.WhileStatementContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("WhileStatement");
    Node node = new Node(treeNode);
    if (!getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("boolean"))){
      System.out.println(String.format("%s: expression on if has to be boolean", ctx.start.getLine()));
    } else {
      Integer label1 = myARM.createLabel();
      Integer label2 = myARM.createLabel();
      Integer label3 = myARM.createLabel();
      
      myARM.useLabel();
      node.addInstruction(String.format("L%d:", label1));
      node = shortCircuitExpression(node, ctx.expression(), label2, label3);
      node.addInstruction(String.format("goto L%d", label3));
      myARM.useLabel();
      node.addInstruction(String.format("L%d:", label2));
      myTable.createEnviroment(String.format("%s-WHILE", myTable.peekEnviroment()));
      node.add(visit(ctx.block()));
      myTable.returnEnviroment();
      node.addInstruction(String.format("goto L%d", label1));
      myARM.useLabel();
      node.addInstruction(String.format("L%d:", label3));
    }
    return node;
  }

  @Override public Node visitReturnStatement(ProyectoParser.ReturnStatementContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ReturnStatement");
    Node node = new Node(treeNode);
    if (ctx.expression() != null) {
      node.add(visit(ctx.expression()));
      node.addInstruction(String.format("PUSH T0 T%d", myARM.getLastRegister()));
    };
    return node;
  }

  @Override public Node visitMethodCallStatement(ProyectoParser.MethodCallStatementContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("MethodCallStatement");
    Node node = new Node(treeNode);
    node.add(visit(ctx.methodCall()));

    Data methodDefinition = myTable.getVariable(ctx.methodCall().ID().getText());
    if (methodDefinition == null) {
      System.out.println(String.format("%s: method <%s> hasn't been declarated", ctx.start.getLine(), ctx.methodCall().ID().getText()));
    } else {
      
    }

    return node;
  }

  @Override public Node visitBlockStatement(ProyectoParser.BlockStatementContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("BlockStatement");
    Node node = new Node(treeNode);
    node.add(visit(ctx.block()));
    return node;
  }

  @Override public Node visitLocationStatement(ProyectoParser.LocationStatementContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("LocationStatement");
    Node node = new Node(treeNode);
    node.add(visit(ctx.expression()));
    node.add(visit(ctx.location()));
    try {
      Pair<String, Integer> tipoLocation = getLocationType(ctx.location());
      Pair<String, Integer> tipoExpression = getExpressionType(ctx.expression());
      if (tipoLocation == null) {
        System.out.println(String.format("%s: location doenst exists", ctx.start.getLine()));
      } else if (tipoExpression == null) {
        System.out.println(String.format("%s: expression doenst exists", ctx.start.getLine()));
      } else if(!tipoLocation.getFirst().equals(tipoExpression.getFirst())){
        System.out.println(String.format("%s: Check types of location and expression", ctx.start.getLine()));
      } else {
        if(ctx.location().location() == null){
          String id = ctx.location().ID().getText();
          Pair<String, Integer> pair = myTable.getOffset(id);
          if(ctx.location().expression() != null) {
            Data data = myTable.getVariable(id);
            node.addInstruction(String.format("T%d = %d + (%d * T%d)", myARM.getLastRegister(), pair.getSecond(), data.getSize(), myARM.getRegister()));
            node.addInstruction(String.format("%s[T%d] = T%s", pair.getFirst(), myARM.getLastRegister(), myARM.getLastRegister()));
          } else {
            node.addInstruction(String.format("%s[%d] = T%s", pair.getFirst(), pair.getSecond(), myARM.getLastRegister()));
          }
        } else {
          String id = ctx.location().ID().getText();
          Pair<String, Integer> pair = myTable.getOffset(id);
          Integer localOffset = pair.getSecond();
          Data data = null;
          ProyectoParser.LocationContext localCtx = (ProyectoParser.LocationContext) ctx.location();
          while(localCtx.location() != null){
            if (data != null) {
              data = data.dependencia;
            } else {
              data = myTable.getVariable(localCtx.ID().getText());
            }
            if (data.variables.containsKey(localCtx.location().ID().getText())) {
              data = data.variables.get(localCtx.location().ID().getText());
              localOffset = data.offset;
            } 
            localCtx = localCtx.location();
          }
          node.addInstruction(String.format("%s[%d] = T%s", pair.getFirst(), localOffset, myARM.getLastRegister()));
        }
      }
    } catch (NullPointerException e) {
        System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
    }
    
    return node;
  }

  @Override public Node visitExpressionStatement(ProyectoParser.ExpressionStatementContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionStatement");
    Node node = new Node(treeNode);
    if (ctx.expression() != null){
      node.add(visit(ctx.expression()));
    }
    return node;
  }

  @Override public Node visitLocation(ProyectoParser.LocationContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Location");
    Node node = new Node(treeNode);
    //DefaultMutableTreeNode IDNode = new DefaultMutableTreeNode(ctx.ID().getText());
    //node.add(IDNode);
    if (ctx.expression() != null) {
      if (getExpressionType(ctx.expression()).getFirst().equals(new Pair<String, Integer>("int").getFirst())) {
      //if (getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("Integer"))) {
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

  @Override public Node visitExpressionLocation(ProyectoParser.ExpressionLocationContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionLocation");
    Node node = new Node(treeNode);

    node.add(visit(ctx.location()));
    if(ctx.location().location() == null){
      String id = ctx.location().ID().getText();
      Pair<String, Integer> pair = myTable.getOffset(ctx.location().ID().getText());
      if(ctx.location().expression() != null) {
        Data data = myTable.getVariable(id);
        node.addInstruction(String.format("T%d = %d + (%d * T%d)", myARM.getLastRegister(), pair.getSecond(), data.getSize(), myARM.getRegister()));
        node.addInstruction(String.format("T%d = %s[T%d]", myARM.getRegister(), pair.getFirst(), myARM.getLastRegister()));
      } else {
        node.addInstruction(String.format("T%d = %s[%d]", myARM.getRegister(), pair.getFirst(), pair.getSecond()));
      }
    } else {
      String id = ctx.location().ID().getText();
      Pair<String, Integer> pair = myTable.getOffset(id);
      Integer localOffset = pair.getSecond();
      Data data = null;
      ProyectoParser.LocationContext localCtx = (ProyectoParser.LocationContext) ctx.location();
      while(localCtx.location() != null){
        if (data != null) {
          data = data.dependencia;
        } else {
          data = myTable.getVariable(localCtx.ID().getText());
        }
        if (data.variables.containsKey(localCtx.location().ID().getText())) {
          data = data.variables.get(localCtx.location().ID().getText());
          localOffset = data.offset;
        } 
        localCtx = localCtx.location();
      }
      node.addInstruction(String.format("T%s = %s[%d]", pair.getFirst(), localOffset, myARM.getLastRegister()));
    }

    
    return node;
  }


  @Override public Node visitExpressionMethodCall(ProyectoParser.ExpressionMethodCallContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionMethodCall");
    Node node = new Node(treeNode);
    node.add(visit(ctx.methodCall()));

    Data method = myTable.getVariable(ctx.methodCall().ID().getText()); 
    if (method == null){
      System.out.println(String.format("%s: Expected method <%s> doenst exists", ctx.start.getLine(), ctx.methodCall().ID()));

    } else if(method.tipo.equals(new Pair<String, Integer>("void"))){
      System.out.println(String.format("%s: Expected return value on <%s>", ctx.start.getLine(), method.id));
    }
    return node;
  }

  @Override public Node visitExpressionLiteral(ProyectoParser.ExpressionLiteralContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionLiteral");
    Node node = new Node(treeNode);
    node.add(visit(ctx.literal()));
    if(ctx.literal().getClass() == ProyectoParser.IntLiteralContext.class){
      ProyectoParser.IntLiteralContext literalType = (ProyectoParser.IntLiteralContext) ctx.literal();
      node.addInstruction(String.format("T%d = %s", myARM.getRegister(), literalType.NUM().getText()));

    } else if(ctx.literal().getClass() == ProyectoParser.CharLiteralContext.class){
      ProyectoParser.CharLiteralContext literalType = (ProyectoParser.CharLiteralContext) ctx.literal();
      node.addInstruction(String.format("T%d = %s", myARM.getRegister(), literalType.CHAR().getText()));

    } else if (ctx.literal().getClass() == ProyectoParser.BoolLiteralContext.class){
      ProyectoParser.BoolLiteralContext literalType = (ProyectoParser.BoolLiteralContext) ctx.literal();
      node.addInstruction(String.format("T%d = %s", myARM.getRegister(), literalType.BOOL().getText()));

    } else {
      System.out.println("unknown literal");
    }
    return node;
  }


  @Override public Node visitExpressionCommon(ProyectoParser.ExpressionCommonContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionCommon");
    Node node = new Node(treeNode);
    node.add(visit(ctx.expression(0)));
    node.add(visit(ctx.op()));
    node.add(visit(ctx.expression(1)));

    Integer temp2 = myARM.getLastRegister();
    Integer temp1 = myARM.getLastRegister();
    try {
      Pair<String, Integer> tipo0 = getExpressionType(ctx.expression(0));
      Pair<String, Integer> tipo1 = getExpressionType(ctx.expression(1));

      if(ctx.op().highArithOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("int")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be integer", ctx.start.getLine()));
        } else {
          if(ctx.op().highArithOp().simbol.getText().equals("*")){
            node.addInstruction(String.format("T%d = T%d * T%d", myARM.getRegister(), temp1, temp2));
          } else {
            node.addInstruction(String.format("T%d = T%d / T%d", myARM.getRegister(), temp1, temp2));
          }
        }
      } else if(ctx.op().arithOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("int")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be integer", ctx.start.getLine()));
        } else {
          if(ctx.op().arithOp().simbol.getText().equals("+")){
            node.addInstruction(String.format("T%d = T%d + T%d", myARM.getRegister(), temp1, temp2));
          } else if(ctx.op().arithOp().simbol.getText().equals("-")) {
            node.addInstruction(String.format("T%d = T%d - T%d", myARM.getRegister(), temp1, temp2));
          } else {
            node.addInstruction(String.format("T%d = T%d %s T%d", myARM.getRegister(), temp1, "%", temp2));
          }
        }
      } else if(ctx.op().relOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("int")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be integer", ctx.start.getLine()));
        } else {
          if(ctx.op().relOp().simbol.getText().equals("<")){
            node.addInstruction(String.format("T%d = T%d < T%d", myARM.getRegister(), temp1, temp2));
          } else if(ctx.op().relOp().simbol.getText().equals(">")){
            node.addInstruction(String.format("T%d = T%d > T%d", myARM.getRegister(), temp1, temp2));
          } else if(ctx.op().relOp().simbol.getText().equals("<=")){
            node.addInstruction(String.format("T%d = T%d <= T%d", myARM.getRegister(), temp1, temp2));
          } else {
            node.addInstruction(String.format("T%d = T%d >= T%d", myARM.getRegister(), temp1, temp2));
          }
        }
      } else if(ctx.op().eqOp() != null) {
        if(!tipo0.getFirst().equals(tipo1.getFirst())) {
          System.out.println(String.format("Check types of %s: expression must be equal", ctx.start.getLine()));
        } else {
          if(ctx.op().eqOp().simbol.getText().equals("==")){
            node.addInstruction(String.format("T%d = T%d == T%d", myARM.getRegister(), temp1, temp2));
          } else {
            node.addInstruction(String.format("T%d = T%d != T%d", myARM.getRegister(), temp1, temp2));
          }
        }
      } else if(ctx.op().condOp() != null) {
        if(!tipo0.equals(new Pair<String, Integer>("boolean")) || !tipo0.equals(tipo1)) {
          System.out.println(String.format("Check types of %s: expression must be boolean", ctx.start.getLine()));
        } else {
          if(ctx.op().condOp().simbol.getText().equals("&&")){
            node.addInstruction(String.format("T%d = T%d && T%d", myARM.getRegister(), temp1, temp2));
          } else {
            node.addInstruction(String.format("T%d = T%d || T%d", myARM.getRegister(), temp1, temp2));
          }
        }
      } else {
        System.out.println(String.format("%s: unavalible op on expression", ctx.start.getLine()));
      }
    } catch (NullPointerException e) {
        System.out.println(String.format("%s: Caught the NullPointerException", ctx.start.getLine()));
    }
    return node;
  }

  @Override public Node visitExpressionNegative(ProyectoParser.ExpressionNegativeContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionNegative");
    Node node = new Node(treeNode);
    node.add(visit(ctx.expression()));
    if(getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("int"))) {
      System.out.println(String.format("%s: Check type of expression must be integer", ctx.start.getLine()));
    }
    Integer last = myARM.getLastRegister();
    node.addInstruction(String.format("T%d = -T%s", myARM.getRegister(), last));
    return node;
  }

  @Override public Node visitExpressionNot(ProyectoParser.ExpressionNotContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionNot");
    Node node = new Node(treeNode);
    node.add(visit(ctx.expression()));
    if(getExpressionType(ctx.expression()).equals(new Pair<String, Integer>("boolean"))) {
      System.out.println(String.format("%s: Check type of expression must be integer", ctx.start.getLine()));
    }
    Integer last = myARM.getLastRegister();
    node.addInstruction(String.format("T%d = !T%s", myARM.getRegister(), last));
    return node;
  }

  @Override public Node visitExpressionGroup(ProyectoParser.ExpressionGroupContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ExpressionGroup");
    Node node = new Node(treeNode);
    node.add(visit(ctx.expression()));
    return node;
  }

  @Override public Node visitMethodCall(ProyectoParser.MethodCallContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("MethodCall");
    Node node = new Node(treeNode);

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
          } else {
            node.addInstruction(String.format("PUSH T%s, T%s", myARM.getParam(), myARM.getLastRegister()));
          }
        } else {
          System.out.println(String.format("%s: more params than needed on method <%s>", ctx.start.getLine(), methodDefinition.id)); 
        }
        i = i+1;
      }
      myARM.freeParams();
      if (methodDefinition.tipo.equals(new Pair<String, Integer>("void"))) {
        node.addInstruction(String.format("CALL %s, %d", methodDefinition.id, i));
      } /**else {
        node.addInstruction(String.format("T0 = CALL %s, %d", methodDefinition.id, i));
      }*/
    }
    return node;
  }

  @Override public Node visitOp(ProyectoParser.OpContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Op");
    Node node = new Node(treeNode);
    if (ctx.highArithOp() != null) {
      node.add(visit(ctx.highArithOp()));
    } else if (ctx.arithOp() != null) {
      node.add(visit(ctx.arithOp()));
    } else if (ctx.relOp() != null) {
      node.add(visit(ctx.relOp()));
    } else if (ctx.eqOp() != null) {
      node.add(visit(ctx.eqOp()));
    } else if (ctx.condOp() != null) {
      node.add(visit(ctx.condOp()));
    };
    //node.addInstruction(node.childs[0].target[0]);
    return node;

  }

  @Override public Node visitHighArithOp(ProyectoParser.HighArithOpContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("HighArithOp");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.simbol.getText());
    return node;
  }

  @Override public Node visitArithOp(ProyectoParser.ArithOpContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("ArithOp");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.simbol.getText());
    return node;
  }

  @Override public Node visitRelOp(ProyectoParser.RelOpContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("RelOp");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.simbol.getText());
    return node;

  }

  @Override public Node visitEqOp(ProyectoParser.EqOpContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("EqOp");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.simbol.getText());
    return node;

  }

  @Override public Node visitCondOp(ProyectoParser.CondOpContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("CondOp");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.simbol.getText());
    return node;

  }

  @Override public Node visitIntLiteral(ProyectoParser.IntLiteralContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("IntLiteral");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.NUM().getText());
    return node;
  }
  
  @Override public Node visitCharLiteral(ProyectoParser.CharLiteralContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("CharLiteral");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.CHAR().getText());
    return node;
  }
  
  @Override public Node visitBoolLiteral(ProyectoParser.BoolLiteralContext ctx) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("BoolLiteral");
    Node node = new Node(treeNode);
    // node.addInstruction(ctx.BOOL().getText());
    return node;
  }
  
}
