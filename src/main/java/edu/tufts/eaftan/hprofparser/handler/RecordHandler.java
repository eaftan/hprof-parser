package edu.tufts.eaftan.hprofparser.handler;

import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.CPUSample;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

/**
 * Primary interface to be used with the hprof parser.  The parser takes an implementation of
 * this interface and calls the matching callback method on each record encountered.
 * Implementations of this interface can do things like printing the record or building a graph.
 * 
 * <p>You may assume that all references passed into the handler methods are non-null.
 * 
 * <p>Generally you want to subclass {@code NullRecordHandler} rather than implement this interface
 * directly. 
 */
public interface RecordHandler {

  public abstract void header(String format, int idSize, long time);

  public abstract void stringInUTF8(long id, String data);

  public abstract void loadClass(int classSerialNum, long classObjId, int stackTraceSerialNum,
      long classNameStringId);

  public abstract void unloadClass(int classSerialNum);

  public abstract void stackFrame(long stackFrameId,
      long methodNameStringId,
      long methodSigStringId,
      long sourceFileNameStringId,
      int classSerialNum,
      int location);

  public abstract void stackTrace(int stackTraceSerialNum, int threadSerialNum, int numFrames,
      long[] stackFrameIds);

  public abstract void allocSites(short bitMaskFlags,
      float cutoffRatio,
      int totalLiveBytes,
      int totalLiveInstances,
      long totalBytesAllocated,
      long totalInstancesAllocated,
      AllocSite[] sites);

  public abstract void heapSummary(int totalLiveBytes, int totalLiveInstances,
      long totalBytesAllocated, long totalInstancesAllocated);

  public abstract void startThread(int threadSerialNum,
      long threadObjectId,
      int stackTraceSerialNum,
      long threadNameStringId,
      long threadGroupNameId,
      long threadParentGroupNameId);

  public abstract void endThread(int threadSerialNum);

  public abstract void heapDump();

  public abstract void heapDumpEnd();

  public abstract void heapDumpSegment();

  public abstract void cpuSamples(int totalNumOfSamples, CPUSample[] samples);

  public abstract void controlSettings(int bitMaskFlags, short stackTraceDepth);

  public abstract void rootUnknown(long objId);

  public abstract void rootJNIGlobal(long objId, long JNIGlobalRefId);

  public abstract void rootJNILocal(long objId, int threadSerialNum, int frameNum);

  public abstract void rootJavaFrame(long objId, int threadSerialNum, int frameNum);

  public abstract void rootNativeStack(long objId, int threadSerialNum);

  public abstract void rootStickyClass(long objId);

  public abstract void rootThreadBlock(long objId, int threadSerialNum);

  public abstract void rootMonitorUsed(long objId);

  public abstract void rootThreadObj(long objId, int threadSerialNum, int stackTraceSerialNum);

  public abstract void classDump(long classObjId,
      int stackTraceSerialNum,
      long superClassObjId,
      long classLoaderObjId,
      long signersObjId,
      long protectionDomainObjId,
      long reserved1,
      long reserved2,
      int instanceSize,
      Constant[] constants,
      Static[] statics,
      InstanceField[] instanceFields);

  public abstract void instanceDump(long objId, int stackTraceSerialNum, long classObjId,
      Value<?>[] instanceFieldValues);

  public abstract void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId,
      long[] elems);

  public abstract void primArrayDump(long objId, int stackTraceSerialNum, byte elemType,
      Value<?>[] elems);

  public abstract void finished();

}
