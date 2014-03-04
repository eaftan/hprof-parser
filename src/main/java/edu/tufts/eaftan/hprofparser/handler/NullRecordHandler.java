/*
 * Copyright 2014 Edward Aftandilian. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.tufts.eaftan.hprofparser.handler;

import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.CPUSample;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

/**
 * Base class to be used with the hprof parser.  For each record the parser encounters, it parses 
 * the record and calls the matching function in its RecordHandler class.  The RecordHandler 
 * handles each record, performing some function such as printing the record or building a graph.
 *
 * The default behavior is to do nothing for any record.
 */
public class NullRecordHandler implements RecordHandler {

  /* handler for file header */
  
  @Override
  public void header(String format, int idSize, long time) {}
  
  
  /* handlers for top-level records */

  @Override
  public void stringInUTF8(long id, String data) {}

  @Override
  public void loadClass(int classSerialNum, long classObjId, 
      int stackTraceSerialNum, long classNameStringId) {}

  @Override
  public void unloadClass(int classSerialNum) {}

  @Override
  public void stackFrame(long stackFrameId, long methodNameStringId, 
      long methodSigStringId, long sourceFileNameStringId, 
      int classSerialNum, int location) {}

  @Override
  public void stackTrace(int stackTraceSerialNum, int threadSerialNum, 
      int numFrames, long[] stackFrameIds) {}

  @Override
  public void allocSites(short bitMaskFlags, float cutoffRatio, 
      int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated,
      long totalInstancesAllocated, AllocSite[] sites) {}

  @Override
  public void heapSummary(int totalLiveBytes, int totalLiveInstances,
      long totalBytesAllocated, long totalInstancesAllocated) {}

  @Override
  public void startThread(int threadSerialNum, long threadObjectId,
      int stackTraceSerialNum, long threadNameStringId, long threadGroupNameId,
      long threadParentGroupNameId) {}

  @Override
  public void endThread(int threadSerialNum) {}

  @Override
  public void heapDump() {}
  
  @Override
  public void heapDumpEnd() {}

  @Override
  public void heapDumpSegment() {}

  @Override
  public void cpuSamples(int totalNumOfSamples, CPUSample[] samples) {}

  @Override
  public void controlSettings(int bitMaskFlags, short stackTraceDepth) {}


  /* handlers for heap dump records */

  @Override
  public void rootUnknown(long objId) {}

  @Override
  public void rootJNIGlobal(long objId, long JNIGlobalRefId) {}

  @Override
  public void rootJNILocal(long objId, int threadSerialNum, int frameNum) {}

  @Override
  public void rootJavaFrame(long objId, int threadSerialNum, int frameNum) {}

  @Override
  public void rootNativeStack(long objId, int threadSerialNum) {}

  @Override
  public void rootStickyClass(long objId) {}

  @Override
  public void rootThreadBlock(long objId, int threadSerialNum) {}

  @Override
  public void rootMonitorUsed(long objId) {}

  @Override
  public void rootThreadObj(long objId, int threadSerialNum, 
      int stackTraceSerialNum) {}

  @Override
  public void classDump(long classObjId, int stackTraceSerialNum, 
      long superClassObjId, long classLoaderObjId, long signersObjId,
      long protectionDomainObjId, long reserved1, long reserved2, 
      int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {}

  @Override
  public void instanceDump(long objId, int stackTraceSerialNum, 
      long classObjId, Value<?>[] instanceFieldValues) {}

  @Override
  public void objArrayDump(long objId, int stackTraceSerialNum, 
      long elemClassObjId, long[] elems) {}

  @Override
  public void primArrayDump(long objId, int stackTraceSerialNum, 
      byte elemType, Value<?>[] elems) {}

  
  /* handler for end of file */
  
  @Override
  public void finished() {}
  
}
