import java.io.*; 
import java.util.*; 
import java.util.HashMap; // import the HashMap class

final class Data {
  private String id;
  private String tipo;
  private String dependencia;
  private Integer cantidad;
  private HashMap<String, Tuple2<String, Integer>> parametros;
  private HashMap<String, Tuple2<String, Integer>> variables;

  //CommonVariable
  public Data(String id, String type) {
    this.id = id;
    this.tipo = type;
  }

  //commonVariable (struct)
  public Data(String dependecy, String id, String type) {
    this.dependencia = dependecy;
    this.id = id;
    this.tipo = type;
  }

  //ArrayVariable
  public Data(String id, String type, Integer size) {
    this.id = id;
    this.tipo = type;
    this.cantidad = size;
  }

  //ArrayVariable (struct)
  public Data(String dependecy, String id, String type, Integer size) {
    this.dependencia = dependecy;
    this.id = id;
    this.tipo = type;
    this.cantidad = size;
  }

  //MethodVariable
  public Data(String id, String type, HashMap<String, String> parameters) {
    this.id = id;
    this.tipo = type;
    this.parametros = parameters;
  }

  //StructVariable
  public Data(String id, HashMap<String, String> variables) {
    this.id = id;
    this.tipo = "struct";
    this.variables = variables;
  }

  public String toString() {
    String cadena = String.format("<%s, %s>", id, tipo);
    return cadena;
  }


}


final class Enviroment {
  public String id;
  private Enviroment father;
  private HashMap<String, Data> localdata = new HashMap<String, Data>();

  public Enviroment(){
    this.id = "global";
  }

  public Enviroment(String id, Enviroment father) {
    this.id = id;
    this.father = father;
  }

  public void putCommonVariable(String type, String id) {
    Data data = new Data(id, type);
    localdata.put(id, data);
  }

  public void putCommonVariable(String dependecy, String type, String id) {
    // revisar que dependency ya haya sido creado aqui o en father
    Data data = new Data(dependecy, id, type);
    localdata.put(id, data);
  }

  public void putArrayVariable(String type, String id, Integer size) {
    Data data = new Data(id, type, size);
    localdata.put(id, data);
  }

  public void putArrayVariable(String dependecy, String type, String id, Integer size) {
    //revisar que dependecy ya haya sido creado
    Data data = new Data(dependecy, id, type, size);
    localdata.put(id, data);
  }

  public void putMethodVariable(String type, String id, HashMap<String, String> parameters) {
    Data data = new Data(id, type, parameters);
    localdata.put(id, data);
  }

  public void putStructVariable(String id, HashMap<String, String> variables){
    Data data = new Data(id, variables);
    localdata.put(id, data);
  }

  public void get(String id) {
    // si esta localData en local data, regresar
    // si no esta tratar de regresar el de los padres
    // si es global y no esta entonces regresar error
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
  private HashMap<String, Enviroment> enviroments = new HashMap<String, Enviroment>();

  public SimbolTable() {
    Enviroment globalEnviroment = new Enviroment();
    enviroments.put("global", globalEnviroment);
    enviromentStack.push("global");
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

  public void putMethodVariable(String type, String id, HashMap<String, String> parameters){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putMethodVariable(type, id, parameters);

  }

  public void putStructVariable(String id, HashMap<String, String> variables){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.putStructVariable(id, variables);
  }

  /*
  //class
  */

  public void getVariable(){
    
  }
}