import java.util.LinkedHashMap;

public class Data {
  public String id;
  public Pair<String, Integer> tipo;
  public String dependencia;
  public LinkedHashMap<String, Pair<String, Integer>> parametros;
  public LinkedHashMap<String, Pair<String, Integer>> variables;

  //CommonVariable
  public Data(String id, String type) {
    this.id = id;
    this.tipo = new Pair<String,Integer>(type);
  }

  //commonVariable (struct)
  public Data(String dependecy, String id, String type) {
    this.dependencia = dependecy;
    this.id = id;
    this.tipo = new Pair<String,Integer>(type);
  }

  //ArrayVariable
  public Data(String id, String type, Integer size) {
    this.id = id;
    this.tipo = new Pair<String,Integer>(type, size);
  }

  //ArrayVariable (struct)
  public Data(String dependecy, String id, String type, Integer size) {
    this.dependencia = dependecy;
    this.id = id;
    this.tipo = new Pair<String,Integer>(type, size);
  }

  //MethodVariable
  public Data(String id, String type, LinkedHashMap<String, Pair<String, Integer>> parameters) {
    this.id = id; 
    this.tipo = new Pair<String,Integer>(type);
    this.parametros = parameters;
  }

  //StructVariable
  public Data(String id, LinkedHashMap<String, Pair<String, Integer>> variables) {
    this.id = id;
    this.tipo = new Pair<String,Integer>("struct");
    this.variables = variables;
  }

  public String toString() {
    /**
    if(parametros != null){
      for (String key : parametros.keySet()) {
        System.out.println(key + ": " + parametros.get(key));
      }
      //System.out.println(parametros.values().toArray()[0]);
      //System.out.println(parametros.entrySet().toArray()[0].toString());
    }
     */
    String cadena = String.format("<%s, %s>", id, tipo.toString());
    return cadena;
  }
}