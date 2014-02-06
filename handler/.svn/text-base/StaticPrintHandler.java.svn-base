package handler;

import handler.summarizehandler.Class;
import java.util.HashMap;
import parser.datastructures.Constant;
import parser.datastructures.InstanceField;
import parser.datastructures.Static;
import parser.datastructures.Type;

public class StaticPrintHandler extends RecordHandler {
  
  private HashMap<Long, String> stringMap = new HashMap<Long, String>();
  private HashMap<Long, Class> classMap = new HashMap<Long, Class>();
  
  public void stringInUTF8(long id, String data) {
    // store string for later lookup
    stringMap.put(id, data);
  }
  
  public void loadClass(int classSerialNum, long classObjId, 
      int stackTraceSerialNum, long classNameStringId) {
    Class cls = new Class();
    cls.classObjId = classObjId;
    cls.className = stringMap.get(classNameStringId);
    classMap.put(classObjId, cls);
  }

  public void classDump(long classObjId, int stackTraceSerialNum, 
      long superClassObjId, long classLoaderObjId, long signersObjId,
      long protectionDomainObjId, long reserved1, long reserved2, 
      int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {
    
    Class cls = classMap.get(classObjId);
    if (cls == null) {
      System.err.println("Error: Could not find class " + classObjId + " from classdump");
      System.exit(1);
    }
    
    if (statics != null) {
      for (Static s: statics) {
        if (s.value.type == Type.OBJ) {
          System.out.println("Static, " + cls.className + ", " + stringMap.get(s.staticFieldNameStringId));
        }
      }
    }
        
  }
  
}
