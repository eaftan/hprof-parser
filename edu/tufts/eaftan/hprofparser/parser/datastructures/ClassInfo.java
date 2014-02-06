package edu.tufts.eaftan.hprofparser.parser.datastructures;

public class ClassInfo {

  public long classObjId;
  public long superClassObjId;
  public int instanceSize;
  public InstanceField[] instanceFields;
  public String className;
  
  public ClassInfo() {}

  public ClassInfo(long classObjId, long superClassObjId, int instanceSize, 
      InstanceField[] instanceFields, String className) {
    this.classObjId = classObjId;
    this.superClassObjId = superClassObjId;
    this.instanceSize = instanceSize;
    this.instanceFields = instanceFields;
    this.className = className;
  }

}
