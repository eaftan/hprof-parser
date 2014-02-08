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

import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;

import edu.tufts.eaftan.hprofparser.handler.NullRecordHandler;
import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Type;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.util.HashMap;

/**
 * Looks for {@code String} instances that contain the same content.
 */
public class StringAnalyzingHandler extends NullRecordHandler {

  private HashMap<Long, String> stringMap = new HashMap<Long, String>();
  private HashMap<Long, ClassInfo> classMap = new HashMap<Long, ClassInfo>();
  private long stringStringId = -1;
  private long stringClassId = -1;
  
  @Override
  public void stringInUTF8(long id, String data) {
    stringMap.put(id, data);
    if (data.equals("java.lang.String")) {
      System.out.println(data);
      stringStringId = id;
    }
  }
  
  @Override
  public void classDump(long classObjId, int stackTraceSerialNum, 
      long superClassObjId, long classLoaderObjId, long signersObjId,
      long protectionDomainObjId, long reserved1, long reserved2, 
      int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {
    if (classObjId == stringClassId) {
      classMap.put(classObjId, new ClassInfo(classObjId, superClassObjId, instanceSize,
          instanceFields));
    }
  }

  @Override
  public void loadClass(int classSerialNum, long classObjId, 
      int stackTraceSerialNum, long classNameStringId) {
    if (classNameStringId == stringStringId) {
      stringClassId = classObjId;
    }
  }

  @Override
  public void instanceDump(long objId, int stackTraceSerialNum, 
      long classObjId, Value<?>[] instanceFieldValues) {
    if (classObjId == stringClassId) {
      System.out.println("Instance Dump:");
      System.out.println("    object id: " + objId);
      System.out.println("    stack trace serial num: " + stackTraceSerialNum);
      System.out.println("    class object id: " + classObjId);

      if (instanceFieldValues.length > 0) {
        System.out.println("    instance field values:");

        // superclass of Object is 0
        int i = 0;
        long nextClass = classObjId;
        while (nextClass != 0) {
          ClassInfo ci = classMap.get(nextClass);
          nextClass = ci.superClassObjId;
          for (InstanceField field : ci.instanceFields) {
            System.out.print("        " + stringMap.get(field.fieldNameStringId));
            System.out.println(" = " + instanceFieldValues[i]);
            i++;
          }
        }
        assert i == instanceFieldValues.length;
      }
    }
  }

  @Override
  public void primArrayDump(long objId, int stackTraceSerialNum, 
      byte elemTypeTag, Value<?>[] elems) {
    Type elemType = Type.hprofTypeToEnum(elemTypeTag);
    if (elemType == Type.CHAR && elems.length > 0) {
      System.out.println("Primitive Array Dump:");
      System.out.println("    object id: " + objId);
      System.out.println("    stack trace serial num: " + stackTraceSerialNum);
      System.out.print("    number of elements: ");
      System.out.println(elems.length);
      System.out.println("    element type: " + elemType);

      for (int i=0; i<elems.length; i++) {
        System.out.println("        element " + (i+1) + ": " + elems[i]);
      }
    }
  } 

}
