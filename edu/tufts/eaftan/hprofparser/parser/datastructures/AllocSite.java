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

package edu.tufts.eaftan.hprofparser.parser.datastructures;

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

