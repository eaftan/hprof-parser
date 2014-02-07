package edu.tufts.eaftan.hprofparser.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Records the set of roots in a given heap dump.
 */
public class RootHandler extends NullRecordHandler {
  
  private List<Long> roots = new ArrayList<>(10000);
  
  private void recordRoot(long objId) {
    roots.add(objId);
  }

  @Override
  public void rootUnknown(long objId) {
    recordRoot(objId);
  }

  @Override
  public void rootJNIGlobal(long objId, long JNIGlobalRefId) {
    recordRoot(objId);
  }

  @Override
  public void rootJNILocal(long objId, int threadSerialNum, int frameNum) {
    recordRoot(objId);
  }

  @Override
  public void rootJavaFrame(long objId, int threadSerialNum, int frameNum) {
    recordRoot(objId);
  }

  @Override
  public void rootNativeStack(long objId, int threadSerialNum) {
    recordRoot(objId);
  }

  @Override
  public void rootStickyClass(long objId) {
    recordRoot(objId);
  }

  @Override
  public void rootThreadBlock(long objId, int threadSerialNum) {
    recordRoot(objId);
  }

  @Override
  public void rootMonitorUsed(long objId) {
    recordRoot(objId);
  }

  @Override
  public void rootThreadObj(long objId, int threadSerialNum, int stackTraceSerialNum) {
    recordRoot(objId);
  }
  
  @Override
  public void finished() {
    System.out.println("Encountered " + roots.size() + " roots");
  }

}
