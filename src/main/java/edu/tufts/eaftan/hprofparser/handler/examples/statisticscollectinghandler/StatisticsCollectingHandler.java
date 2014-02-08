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

package edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler;

import com.google.common.primitives.Ints;

import edu.tufts.eaftan.hprofparser.handler.NullRecordHandler;

import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Type;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Computes basic statistics about a heap dump.  For example, the number of instances of each
 * type and the total bytes used by instances of that type.
 * 
 * <p>Note that this is nowhere near accurate.  We intentionally ignore array and object header 
 * sizes because they are VM dependent.  We also don't know how the fields were laid out, what the
 * alignment was, whether OOPs were compressed, etc.  There's no way to get this right without 
 * knowing details about the VM & options that produced it.
 */
public class StatisticsCollectingHandler extends NullRecordHandler {
  private Map<Long, TypeInfo> classMap = new HashMap<>();
  private Map<Long, String> stringMap = new HashMap<>();
  private Map<String, TypeInfo> arrayInfoMap = new HashMap<>();

  @Override
  public void stringInUTF8(long id, String data) {
    stringMap.put(id, data);
  }
  
  @Override
  public void loadClass(int classSerialNum, long classObjId, int stackTraceSerialNum,
      long classNameStringId) {
    ClassInfo classInfo = new ClassInfo();
    classInfo.className = stringMap.get(classNameStringId);
    classMap.put(classObjId, classInfo);
  }
  
  @Override
  public void classDump(long classObjId, int stackTraceSerialNum, long superClassObjId, 
      long classLoaderObjId, long signersObjId, long protectionDomainObjId, long reserved1,
      long reserved2, int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {
    ClassInfo classInfo = (ClassInfo) classMap.get(classObjId);
    classInfo.instanceSize = instanceSize;
  }


  @Override
  public void instanceDump(long objId, int stackTraceSerialNum, long classObjId,
      Value<?>[] instanceFieldValues) {
    ClassInfo classInfo = (ClassInfo) classMap.get(classObjId);
    classInfo.instanceCount++;
  }

  @Override
  public void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId, long[] elems) {
    String typeDescriptor = "[" + classMap.get(elemClassObjId).className;
    int length = elems != null ? elems.length : 0;  
    recordArrayInstance(typeDescriptor, length * Type.OBJ.sizeInBytes());
  }

  @Override
  public void primArrayDump(long objId, int stackTraceSerialNum, byte hprofElemType, Value<?>[] elems) {
    Type elemType = Type.hprofTypeToEnum(hprofElemType);
    String typeDescriptor = "[" + elemType.toString();
    int length = elems != null ? elems.length : 0;  
    recordArrayInstance(typeDescriptor, length * elemType.sizeInBytes());
  }

  private void recordArrayInstance(String typeDescriptor, int bytes) {
    ArrayInfo arrayInfo = (ArrayInfo) arrayInfoMap.get(typeDescriptor);
    if (arrayInfo == null) {
      arrayInfo = new ArrayInfo();
      arrayInfo.className = typeDescriptor;
      arrayInfoMap.put(typeDescriptor, arrayInfo);
    }
    arrayInfo.instanceCount++;
    arrayInfo.totalSize += bytes;
  }
  
  @Override
  public void finished() {
    Comparator<TypeInfo> totalSizeComparator = new Comparator<TypeInfo>() {
      @Override
      public int compare(TypeInfo cls1, TypeInfo cls2) {
        return Ints.checkedCast(cls2.totalSize() - cls1.totalSize());
      }
    };
    
    List<TypeInfo> typeInfoList = new ArrayList<>(classMap.values());
    typeInfoList.addAll(arrayInfoMap.values());
    Collections.sort(typeInfoList, totalSizeComparator);
    for (TypeInfo typeInfo : typeInfoList) {
      if (typeInfo.instanceCount == 0) {
        continue;
      }
      System.out.println(typeInfo.toString());
    }
  }
   

}
