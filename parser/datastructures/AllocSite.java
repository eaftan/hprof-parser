package parser.datastructures;

public class AllocSite {

  public byte arrayIndicator;
  public int classSerialNum;
  public int stackTraceSerialNum;
  public int numLiveBytes;
  public int numLiveInstances;
  public int numBytesAllocated;
  public int numInstancesAllocated;

  public AllocSite(byte arrayIndicator, int classSerialNum, 
      int stackTraceSerialNum, int numLiveBytes, int numLiveInstances, 
      int numBytesAllocated, int numInstancesAllocated) {

    this.arrayIndicator = arrayIndicator;
    this.classSerialNum = classSerialNum;
    this.stackTraceSerialNum = stackTraceSerialNum;
    this.numLiveBytes = numLiveBytes;
    this.numLiveInstances = numLiveInstances;
    this.numBytesAllocated = numBytesAllocated;
    this.numInstancesAllocated = numInstancesAllocated;

  }

}

