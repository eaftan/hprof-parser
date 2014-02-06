package parser.datastructures;

public enum Type {

  OBJ("Object"), BOOL("boolean"), CHAR("char"), FLOAT("float"), DOUBLE("double"), 
  BYTE("byte"), SHORT("short"), INT("int"), LONG("long");
  
  private final String name;
  
  private Type(String name) {
    this.name = name;
  }

  public static Type hprofTypeToEnum(byte type) {
    switch (type) {
      case 2:
        return OBJ;
      case 4:
        return BOOL;
      case 5:
        return CHAR;
      case 6:
        return FLOAT;
      case 7:
        return DOUBLE;
      case 8:
        return BYTE;
      case 9:
        return SHORT;
      case 10:
        return INT;
      case 11:
        return LONG;
      default:
        System.err.println("Error: unsupported type " + type);
        System.exit(1);
        break;
    }

    return null;
  }
    
  public String toString() {
    return name;
  }

}


