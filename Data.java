import java.util.LinkedHashMap;
import java.util.*; 

public class Data {
  public String id;
  public Pair<String, Integer> tipo;
  public Data dependencia;
  public Integer offset;
  public Integer size = 0;
  public LinkedHashMap<String, Data> variables;
  public LinkedHashMap<String, Pair<String, Integer>> parametros;

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
  public Data(Data dependecy, String id, String type, Integer offset) {
    this.dependencia = dependecy;
    this.offset = offset;
    this.id = id;
    this.tipo = new Pair<String,Integer>(type);
    this.size = dependecy.size;
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
    this.dependencia = dependecy;
    this.offset = offset;
    this.id = id;
    this.tipo = new Pair<String,Integer>(type, size);
    this.size = dependecy.size * size;
  }

  //StructVariable
  public Data(String id, List<Data> variables, Integer offset) {
    this.id = id;
    this.offset = offset;
    this.tipo = new Pair<String,Integer>("struct");
    this.variables = new LinkedHashMap<String, Data>();
    for (Data variable : variables) {
      this.size = this.size + variable.size;
      this.variables.put(variable.id, variable);
    }
  }

  //methodVariable
  public Data(String id, String type, LinkedHashMap<String, Pair<String, Integer>> parameters, Integer offset) {
    this.id = id; 
    this.offset = offset;
    this.tipo = new Pair<String,Integer>(type);
    this.parametros = parameters;
    this.size = 0;
  }

  public String toString() {
    String cadena = String.format("<%s, %s>", id, tipo.toString());
    return cadena;
  }
}