import java.util.Stack;
import java.util.LinkedHashMap;
import java.util.*; 

final class Enviroment {
  public String id;
  private Enviroment father;
  private Integer offset;
  private LinkedHashMap<String, Data> localdata = new LinkedHashMap<String, Data>();

  public Enviroment(){
    this.id = "global";
    this.offset = 0;
  }

  public Enviroment(String id, Enviroment father) {
    this.id = id;
    this.father = father;
    this.offset = 0;
  }

  public Integer putCommonVariable(String type, String id) {
    if (! localdata.containsKey(id)) {
      Data data = new Data(id, type, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    } 
    System.out.println(String.format("duplicate value %s", id));
    return -1;
  }

  public Integer putCommonVariable(String dependecy, String type, String id) {
    if (! localdata.containsKey(id)) {
      Data data = new Data(getVariable(dependecy), id, type, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    } 
    System.out.println(String.format("duplicate value %s", id));
    return -1;
  }

  public Integer putArrayVariable(String type, String id, Integer size) {
    if (! localdata.containsKey(id)) {
      Data data = new Data(id, type, size, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    }
    System.out.println(String.format("duplicate value %s", id));
    return -1;
  }

  public Integer putArrayVariable(String dependecy, String type, String id, Integer size) {
    if (! localdata.containsKey(id)) {
      Data data = new Data(getVariable(dependecy), id, type, size, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    } 
    System.out.println(String.format("duplicate value %s", id));
    return -1;
  }

  public Integer putStructVariable(String id, List<Data> variables){
    if (! localdata.containsKey(id)) {
      Data data = new Data(id, variables, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    }
    System.out.println(String.format("duplicate value %s, ", id));
    return 0;
  }

  public Integer putMethodVariable(String type, String id, LinkedHashMap<String, Pair<String, Integer>> parameters) {
    if (! localdata.containsKey(id)) {
      Data data = new Data(id, type, parameters, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    }
    System.out.println(String.format("duplicate value %s", id));
    return 0;
  }

  public Data getVariable(String id) {
    if (localdata.containsKey(id)) {
      Data data = localdata.get(id);
      if(data.tipo.getFirst().equals("struct") && data.dependencia != null){
        return data.dependencia;
      }
      return data;
    } else if (father != null){
      return father.getVariable(id);
    }
    System.out.println(String.format("variable %s doesnt exists", id));
    return null;
  }

  public Pair<String, Integer> getStructVariable(ProyectoParser.LocationContext ctx) {
    Pair<String, Integer> tipo = null;
    Data data = null;
    while(ctx.location() != null){
      if (tipo != null) {
        data = data.dependencia;
      } else {
        data = getVariable(ctx.ID().getText());
      }
      if (data.variables.containsKey(ctx.location().ID().getText())) {
        data = data.variables.get(ctx.location().ID().getText());
        tipo = data.tipo;
      } 
      ctx = ctx.location();
    }
    return tipo;
  }

  public String toString(){
    String cadena;
    if (id == "global") {
      cadena = String.format("(<%s> %s)", "none", localdata.toString());
    } else {
      cadena = String.format("(<%s> %s)", father.id, localdata.toString());
    }
    return cadena;
  }
 
}

public class SimbolTable {
  private Stack<String> enviromentStack = new Stack<String>(); 
  private LinkedHashMap<String, Enviroment> enviroments = new LinkedHashMap<String, Enviroment>();

  public SimbolTable() {
    Enviroment globalEnviroment = new Enviroment();
    enviroments.put("global", globalEnviroment);
    enviromentStack.push("global");
  }

  public Data isMainCreated(){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    Data main = currentEnviroment.getVariable("main");
    if (currentEnviroment.id == "global" &&  main != null) {
      return main;
    } 
    return null;
  }

  public void show(){
    System.out.println(enviroments.toString());
  }

  public void createEnviroment(String id) {
    Enviroment fatherEnviroment = enviroments.get(enviromentStack.peek());
    Enviroment newEnviroment = new Enviroment(id, fatherEnviroment);
    enviroments.put(id, newEnviroment);
    enviromentStack.push(id);
  }

  public String peekEnviroment() {
    return enviroments.get(enviromentStack.peek()).id;
  }

  public String returnEnviroment(){
    // revisar que no haga pop de global
    String enviroment = enviromentStack.peek();
    if (enviroment != "global") {
      enviromentStack.pop();
    } else {
      // alegar que no se pase de global
    }
    return enviroment;
  }

  // commonVariable
  public Pair<String, Integer> putCommonVariable(String type, String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putCommonVariable(type, id));
  }

  // commonVariable (struct)
  public Pair<String, Integer> putCommonVariable(String dependecy, String type, String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putCommonVariable(dependecy, type, id));
  }

  // arrayVariable
  public Pair<String, Integer> putArrayVariable(String type, String id, Integer size){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putArrayVariable(type, id, size));
  }

  // arrayVariable (struct)
  public Pair<String, Integer> putArrayVariable(String dependecy, String type, String id, Integer size){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putArrayVariable(dependecy, type, id, size));
  }
  
  // structVariable
  public Pair<String, Integer> putStructVariable(String id, List<Data> variables){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putStructVariable(id, variables));
  }

  public Pair<String, Integer> putMethodVariable(String type, String id, LinkedHashMap<String, Pair<String, Integer>> parameters){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putMethodVariable(type, id, parameters));
  }

  /*
  //class
  */

  public Data getVariable(String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return currentEnviroment.getVariable(id);
  }

  public Pair<String, Integer> getOffset(String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer> (currentEnviroment.id, currentEnviroment.getVariable(id).offset);
  }

  public Pair<String, Integer> getStructVariable(ProyectoParser.LocationContext ctx){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return currentEnviroment.getStructVariable(ctx);
  }
}