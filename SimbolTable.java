import java.io.*; 
import java.util.*; 
import java.util.HashMap; // import the HashMap class

final class Data {
  private String id;
  private String tipo;

  private String direccion;
  private String tamaño;

  private String parametros;
  private String ambito;
  private String numeroLinea;
  private String cantidad;

  public static void main() {

  }
}


final class Enviroment {
  private String id;
  private Enviroment father;
  private HashMap<String, Data> localdata = new HashMap<String, Data>();

  public void main() {
    this.id = "global";
  }

  public void main(String id, Enviroment father) {
    this.id = id;
    this.father = father;
  }

  public static void put(String type, String id){

  }
}

public class SimbolTable {
  private Stack<String> enviromentStack = new Stack<String>(); 
  private HashMap<String, Enviroment> enviroments = new HashMap<String, Enviroment>();

  public void main() {
    Enviroment globalEnviroment = new Enviroment();
    enviroments.put("global", globalEnviroment);
    enviromentStack.push("global");
  }

  public void createEnviroment(String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    new Enviroment(id, currentEnviroment)
    Enviroment newEnviroment = new Enviroment(id, currentEnviroment);
    enviromentStack.push(id);
  }

  public String returnEnviroment(){
    // revisar que no haga pop de global
    String enviroment = enviromentStack.pop();
    return enviroment;
  }

  // commonVariable
  public void putVariable(String type, String id){
    Enviroment currentEnviroment = enviroments.get(enviromentStack.peek());
    currentEnviroment.put(type, id);
  }

  // arrayVariable
  public void putVariable(String type, String id, int size){
    
  }

  // method
  public void putVariable(String type, String id, String[] parameters){
    
  }

  // Struct
  public void putVariable(String id, String[] declarations1){
    
  }

  //class

  public void getVariable(){
    
  }
}