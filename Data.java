import java.util.HashMap;

public class Data {
  private String id;
  private String tipo;
  private String dependencia;
  private Integer cantidad;
  private HashMap<String, Pair<String, Integer>> parametros;
  private HashMap<String, Pair<String, Integer>> variables;

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
  public Data(String id, String type, HashMap<String, Pair<String, Integer>> parameters) {
    this.id = id;
    this.tipo = type;
    this.parametros = parameters;
  }

  //StructVariable
  public Data(String id, HashMap<String, Pair<String, Integer>> variables) {
    this.id = id;
    this.tipo = "struct";
    this.variables = variables;
  }

  public String toString() {
    String cadena = String.format("<%s, %s>", id, tipo);
    return cadena;
  }
}