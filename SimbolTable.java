import java.util.Stack;
import java.util.LinkedHashMap;

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
    // revisar que dependency ya haya sido creado aqui o en father
    if (! localdata.containsKey(id)) {
      Data data = new Data(dependecy, id, type, offset);
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
      //revisar que dependecy ya haya sido creado
      Data dependecyData = getVariable(dependecy);
      Data data = new Data(dependecyData, id, type, size, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    } 
    System.out.println(String.format("duplicate value %s", id));
    return -1;
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

  public Integer putStructVariable(String id, LinkedHashMap<String, Pair<String, Integer>> variables){
    if (! localdata.containsKey(id)) {
      Data data = new Data(id, variables, offset);
      localdata.put(id, data);
      offset = offset + data.size;
      return data.offset;
    }
    System.out.println(String.format("duplicate value %s, ", id));
    return 0;
  }

  public Data getVariable(String id) {
    if (localdata.containsKey(id)) {
      Data data = localdata.get(id);
      if(data.tipo.getFirst().equals("struct") && data.dependencia != null){
        return getVariable(data.dependencia);
      }
      return data;
    } else if (father != null){
      return father.getVariable(id);
    }
    return null;
  }

  //arreglar
  public Pair<String, Integer> getStructVariable(ProyectoParser.LocationContext ctx) {
    if (localdata.containsKey(ctx.ID().getText())){
      if (ctx.location().location() == null) {
        Data dependencia = getVariable(localdata.get(ctx.ID().getText()).dependencia);
        return dependencia.variables.get(ctx.location().ID().getText());
      } else if(ctx.location().location().location() == null){
        Data dependencia = getVariable(localdata.get(ctx.ID().getText()).dependencia);
        Pair<String, Integer> test = dependencia.variables.get(ctx.location().ID().getText());
        return getVariable(test.getFirst()).variables.get(ctx.location().location().ID().getText());
      } else{
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
  public Pair<String, Integer> putArrayVariable(String type, String id, int size){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putArrayVariable(type, id, size));
  }

  // arrayVariable (struct)
  public Pair<String, Integer> putArrayVariable(String dependecy, String type, String id, int size){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putArrayVariable(dependecy, type, id, size));
  }

  public Pair<String, Integer> putMethodVariable(String type, String id, LinkedHashMap<String, Pair<String, Integer>> parameters){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putMethodVariable(type, id, parameters));

  }

  public Pair<String, Integer> putStructVariable(String id, LinkedHashMap<String, Pair<String,Integer>> variables){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    return new Pair<String, Integer>(currentEnviroment.id, currentEnviroment.putStructVariable(id, variables));
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