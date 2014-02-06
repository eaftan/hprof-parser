package edu.tufts.eaftan.hprofparser.handler;

import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Type;

import java.util.HashMap;

public class StaticPrintHandler extends RecordHandler {
  
  private HashMap<Long, String> stringMap = new HashMap<Long, String>();
  private HashMap<Long, ClassInfo> classMap = new HashMap<Long, ClassInfo>();
  
  @Override
  public void stringInUTF8(long id, String data) {
    // store string for later lookup
    stringMap.put(id, data);
  }
  
  @Override
  public void loadClass(int classSerialNum, long classObjId, 
      int stackTraceSerialNum, long classNameStringId) {
    ClassInfo cls = new ClassInfo();
    cls.classObjId = classObjId;
    cls.className = stringMap.get(classNameStringId);
    classMap.put(classObjId, cls);
  }

  @Override
  public void classDump(long classObjId, int stackTraceSerialNum, 
      long superClassObjId, long classLoaderObjId, long signersObjId,
      long protectionDomainObjId, long reserved1, long reserved2, 
      int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {
    
    ClassInfo cls = classMap.get(classObjId);
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
