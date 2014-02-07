package edu.tufts.eaftan.hprofparser.handler;

import com.google.common.primitives.Ints;

import edu.tufts.eaftan.hprofparser.parser.datastructures.Type;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects basic statistics about a heap dump.  For example, the number of instances of each
 * type.
 */
public class StatisticsCollectingHandler extends NullRecordHandler {
  
  // TODO(eaftan): Use more accurate values.  Correct values are in 
  // hotspot/src/share/vm/oops/markOop.hpp and trunk/hotspot/src/share/vm/oops/arrayOop.hpp.
  private static final int OBJECT_HEADER_SIZE_BYTES = 4;
  private static final int ARRAY_HEADER_SIZE_BYTES = OBJECT_HEADER_SIZE_BYTES + 4;
  
  private static class ClassInfo {
    public String className;
    public long instanceCount = 0;
    public long totalBytes = 0;
    
    @Override
    public String toString() {
      return className + ": " + totalBytes + " bytes, "+ instanceCount + " instances";
    }
  }
  
  private Map<Long, ClassInfo> classMap = new HashMap<>();
  private Map<Long, String> stringMap = new HashMap<>();
  private Map<String, ClassInfo> arrayInfoMap = new HashMap<>();

  @Override
  public void stringInUTF8(long id, String data) {
    stringMap.put(id, data);
  }
  
  @Override
  public void loadClass(int classSerialNum, long classObjId, int stackTraceSerialNum,
      long classNameStringId) {
    ClassInfo classInfo = new ClassInfo();
    String className = stringMap.get(classNameStringId);
    classInfo.className = className;
    classMap.put(classObjId, classInfo);
  }

  @Override
  public void instanceDump(long objId, int stackTraceSerialNum, long classObjId,
      Value[] instanceFieldValues) {
    ClassInfo classInfo = classMap.get(classObjId);
    classInfo.instanceCount++;
  }

  @Override
  public void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId, long[] elems) {
    String typeDescriptor = "[" + classMap.get(elemClassObjId).className;
    int length = elems != null ? elems.length : 0;  
    recordInstance(typeDescriptor, ARRAY_HEADER_SIZE_BYTES + length * Type.OBJ.sizeInBytes());
  }

  @Override
  public void primArrayDump(long objId, int stackTraceSerialNum, byte hprofElemType, Value[] elems) {
    Type elemType = Type.hprofTypeToEnum(hprofElemType);
    String typeDescriptor = "[" + elemType.toString();
    int length = elems != null ? elems.length : 0;  
    recordInstance(typeDescriptor, ARRAY_HEADER_SIZE_BYTES + length * elemType.sizeInBytes());
  }

  private void recordInstance(String typeDescriptor, int bytes) {
    ClassInfo classInfo = arrayInfoMap.get(typeDescriptor);
    if (classInfo == null) {
      classInfo = new ClassInfo();
      classInfo.className = typeDescriptor;
      arrayInfoMap.put(typeDescriptor, classInfo);
    }
    classInfo.instanceCount++;
    classInfo.totalBytes += bytes;
  }
  
  @Override
  public void finished() {
    Comparator<ClassInfo> instanceCountComparator = new Comparator<ClassInfo>() {
      @Override
      public int compare(ClassInfo cls1, ClassInfo cls2) {
        return Ints.checkedCast(cls2.instanceCount - cls1.instanceCount);
      }
    };
    
    List<ClassInfo> classInfoList = new ArrayList<>(classMap.values());
    classInfoList.addAll(arrayInfoMap.values());
    Collections.sort(classInfoList, instanceCountComparator);
    for (ClassInfo classInfo : classInfoList) {
      if (classInfo.instanceCount == 0) {
        continue;
      }
      System.out.println(classInfo.toString());
    }
  }
   

}
