/****************************************************************************
 * This is a base class to be used with the hprof parser.  For each 
 * record the parser encounters, it parses the record and calls the
 * matching function in its RecordHandler class.  The RecordHandler 
 * handles each record, performing some function such as printing the
 * record or building a graph.
 *
 * This base class does nothing for any record.
 * **************************************************************************/

package edu.tufts.eaftan.hprofparser.handler;

import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.CPUSample;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

public class RecordHandler {

  /* handler for file header */
  
  public void header(String format, int idSize, long time) {}
  
  
  /* handlers for top-level records */

  public void stringInUTF8(long id, String data) {}

  public void loadClass(int classSerialNum, long classObjId, 
      int stackTraceSerialNum, long classNameStringId) {}

  public void unloadClass(int classSerialNum) {}

  public void stackFrame(long stackFrameId, long methodNameStringId, 
      long methodSigStringId, long sourceFileNameStringId, 
      int classSerialNum, int location) {}

  public void stackTrace(int stackTraceSerialNum, int threadSerialNum, 
      int numFrames, long[] stackFrameIds) {}

  public void allocSites(short bitMaskFlags, float cutoffRatio, 
      int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated,
      long totalInstancesAllocated, AllocSite[] sites) {}

  public void heapSummary(int totalLiveBytes, int totalLiveInstances,
      long totalBytesAllocated, long totalInstancesAllocated) {}

  public void startThread(int threadSerialNum, long threadObjectId,
      int stackTraceSerialNum, long threadNameStringId, long threadGroupNameId,
      long threadParentGroupNameId) {}

  public void endThread(int threadSerialNum) {}

  public void heapDump() {}
  
  public void heapDumpEnd() {}

  public void heapDumpSegment() {}

  public void cpuSamples(int totalNumOfSamples, CPUSample[] samples) {}

  public void controlSettings(int bitMaskFlags, short stackTraceDepth) {}


  /* handlers for heap dump records */

  public void rootUnknown(long objId) {}

  public void rootJNIGlobal(long objId, long JNIGlobalRefId) {}

  public void rootJNILocal(long objId, int threadSerialNum, int frameNum) {}

  public void rootJavaFrame(long objId, int threadSerialNum, int frameNum) {}

  public void rootNativeStack(long objId, int threadSerialNum) {}

  public void rootStickyClass(long objId) {}

  public void rootThreadBlock(long objId, int threadSerialNum) {}

  public void rootMonitorUsed(long objId) {}

  public void rootThreadObj(long objId, int threadSerialNum, 
      int stackTraceSerialNum) {}

  public void classDump(long classObjId, int stackTraceSerialNum, 
      long superClassObjId, long classLoaderObjId, long signersObjId,
      long protectionDomainObjId, long reserved1, long reserved2, 
      int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {}

  public void instanceDump(long objId, int stackTraceSerialNum, 
      long classObjId, Value[] instanceFieldValues) {}

  public void objArrayDump(long objId, int stackTraceSerialNum, 
      long elemClassObjId, long[] elems) {}

  public void primArrayDump(long objId, int stackTraceSerialNum, 
      byte elemType, Value[] elems) {}

  
  /* handler for end of file */
  
  public void finished() {}
  
}
