package edu.tufts.eaftan.hprofparser.parser.datastructures;

public class Instance {

  public long objId;
  public int stackTraceSerialNum;
  public long classObjId;
  public byte[] packedValues;    // will use ByteArrayInputStream to read this

  public Instance(long objId, int stackTraceSerialNum, long classObjId,
      byte[] packedValues) {
    this.objId = objId;
    this.stackTraceSerialNum = stackTraceSerialNum;
    this.classObjId = classObjId;
    this.packedValues = packedValues;
  }

}

