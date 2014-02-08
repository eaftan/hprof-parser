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

package edu.tufts.eaftan.hprofparser.handler.examples;

import edu.tufts.eaftan.hprofparser.handler.NullRecordHandler;

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
