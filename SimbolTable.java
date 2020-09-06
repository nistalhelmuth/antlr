import java.util.Stack;
import java.util.LinkedHashMap;

final class Enviroment {
  public String id;
  private Enviroment father;
  private LinkedHashMap<String, Data> localdata = new LinkedHashMap<String, Data>();

  public Enviroment(){
    this.id = "global";
  }

  public Enviroment(String id, Enviroment father) {
    this.id = id;
    this.father = father;
  }

  public void putCommonVariable(String type, String id) {
    Data data = new Data(id, type);
    if (! localdata.containsKey(id)) {
      localdata.put(id, data);
    } else {
      System.out.println(String.format("duplicate value %s", id));
    }
  }

  public void putCommonVariable(String dependecy, String type, String id) {
    // revisar que dependency ya haya sido creado aqui o en father
    Data data = new Data(dependecy, id, type);
    if (! localdata.containsKey(id)) {
      localdata.put(id, data);
    } else {
      System.out.println(String.format("duplicate value %s", id));
    }
  }

  public void putArrayVariable(String type, String id, Integer size) {
    Data data = new Data(id, type, size);
    if (! localdata.containsKey(id)) {
      localdata.put(id, data);
    } else {
      System.out.println(String.format("duplicate value %s", id));
    }
  }

  public void putArrayVariable(String dependecy, String type, String id, Integer size) {
    //revisar que dependecy ya haya sido creado
    Data data = new Data(dependecy, id, type, size);
    if (! localdata.containsKey(id)) {
      localdata.put(id, data);
    } else {
      System.out.println(String.format("duplicate value %s", id));
    }
  }

  public void putMethodVariable(String type, String id, LinkedHashMap<String, Pair<String, Integer>> parameters) {
    Data data = new Data(id, type, parameters);
    if (! localdata.containsKey(id)) {
      localdata.put(id, data);
    } else {
      System.out.println(String.format("duplicate value %s", id));
    }
  }

  public void putStructVariable(String id, LinkedHashMap<String, Pair<String, Integer>> variables){
    Data data = new Data(id, variables);
    if (! localdata.containsKey(id)) {
      localdata.put(id, data);
    } else {
      System.out.println(String.format("duplicate value %s, ", id));
    }
  }

  public Data getVariable(String id) {
    if (localdata.containsKey(id)) {
      return localdata.get(id);
    } else if (father != null){
      return father.getVariable(id);
    }
    return null;
  }

  public Pair<String, Integer> getStructVariable(ProyectoParser.LocationContext ctx) {
    //System.out.println(id);
    if (localdata.containsKey(ctx.ID().getText())){
      //System.out.println("contains");
      
      if (ctx.location().location() == null) {
        //System.out.println(ctx.location().location() == null);
        //System.out.println(localdata.get(ctx.ID().getText()).dependencia);
        Data dependencia = getVariable(localdata.get(ctx.ID().getText()).dependencia);
        //System.out.println(dependencia);
        //System.out.println(dependencia.variables);
        //System.out.println(ctx.location().ID().getText());
        //System.out.println();
        
        return dependencia.variables.get(ctx.location().ID().getText());
      } else {
        return getStructVariable(ctx.location());
      }
    } else if(father != null){
      return father.getStructVariable(ctx);
    }
    return null;
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
  public void putCommonVariable(String type, String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putCommonVariable(type, id);
  }

  // commonVariable (struct)
  public void putCommonVariable(String dependecy, String type, String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putCommonVariable(dependecy, type, id);
  }

  // arrayVariable
  public void putArrayVariable(String type, String id, int size){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putArrayVariable(type, id, size);
  }

  // arrayVariable (struct)
  public void putArrayVariable(String dependecy, String type, String id, int size){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putArrayVariable(dependecy, type, id, size);
  }

  public void putMethodVariable(String type, String id, LinkedHashMap<String, Pair<String, Integer>> parameters){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putMethodVariable(type, id, parameters);

  }

  public void putStructVariable(String id, LinkedHashMap<String, Pair<String,Integer>> variables){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putStructVariable(id, variables);
  }

  /*
  //class
  */

  public Data getVariable(String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return currentEnviroment.getVariable(id);
  }

  public Pair<String, Integer> getStructVariable(ProyectoParser.LocationContext ctx){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return currentEnviroment.getStructVariable(ctx);
  }
}