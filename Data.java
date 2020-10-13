import java.util.LinkedHashMap;

public class Data {
  public String id;
  public Pair<String, Integer> tipo;
  public String dependencia;
  public Integer offset;
  public Integer size = 0;
  public LinkedHashMap<String, Pair<String, Integer>> parametros;
  public LinkedHashMap<String, Pair<String, Integer>> variables;

  public Integer getSize() {
    if(this.tipo.getFirst().equals("int")){
      return 4;
    } else if (this.tipo.getFirst().equals("boolean")){
      return 1;
    } else if (this.tipo.getFirst().equals("char")){
      return 2;
    }
    return 0;
  }

  public Integer getSize(String type) {
    if(type.equals("int")){
      return 4;
    } else if (type.equals("boolean")){
      return 1;
    } else if (type.equals("char")){
      return 2;
    }
    return 0;
  }

  //CommonVariable
  public Data(String id, String type, Integer offset) {
    this.id = id;
    this.offset = offset;
    this.tipo = new Pair<String,Integer>(type);
    this.size = getSize();
  }

  //commonVariable (struct)
  public Data(String dependecy, String id, String type, Integer offset) {
    this.dependencia = dependecy;
    this.offset = offset;
    this.id = id;
    this.tipo = new Pair<String,Integer>(type);
    this.size = getSize();
  }

  //ArrayVariable
  public Data(String id, String type, Integer size, Integer offset) {
    this.id = id;
    this.offset = offset;
    this.tipo = new Pair<String,Integer>(type, size);
    this.size = getSize() * size;
  }

  //ArrayVariable (struct)
  public Data(Data dependecy, String id, String type, Integer size, Integer offset) {
    this.dependencia = dependecy.id;
    this.offset = offset;
    this.id = id;
    this.tipo = new Pair<String,Integer>(type, size);
    this.size = getSize() * dependecy.size;
  }

  //MethodVariable
  public Data(String id, String type, LinkedHashMap<String, Pair<String, Integer>> parameters, Integer offset) {
    this.id = id; 
    this.offset = offset;
    this.tipo = new Pair<String,Integer>(type);
    this.parametros = parameters;
    this.size = 0;
  }

  //StructVariable
  public Data(String id, LinkedHashMap<String, Pair<String, Integer>> variables, Integer offset) {
    this.id = id;
    this.offset = offset;
    this.tipo = new Pair<String,Integer>("struct");
    this.variables = variables;
    for (Pair<String, Integer> variable : variables.values()) {
      if (variable.getSecond() != null) {
        this.size = this.size + getSize(variable.getFirst()) * variable.getSecond();
      } else {
        this.size = this.size + getSize(variable.getFirst());
      }
    }
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