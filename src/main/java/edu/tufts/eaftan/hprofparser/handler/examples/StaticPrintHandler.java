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
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Type;

import java.util.HashMap;

public class StaticPrintHandler extends NullRecordHandler {
  
  private HashMap<Long, String> stringMap = new HashMap<Long, String>();
  private HashMap<Long, ClassInfo> classMap = new HashMap<Long, ClassInfo>();
  
  private static class ClassInfo {
    public final String name;
    public ClassInfo(String name) {
      this.name = name;
    }
  }
  
  @Override
  public void stringInUTF8(long id, String data) {
    // store string for later lookup
    stringMap.put(id, data);
  }
  
  @Override
  public void loadClass(int classSerialNum, long classObjId, 
      int stackTraceSerialNum, long classNameStringId) {
    ClassInfo cls = new ClassInfo(stringMap.get(classNameStringId));
    classMap.put(classObjId, cls);
  }

  @Override
  public void classDump(long classObjId, int stackTraceSerialNum, 
      long superClassObjId, long classLoaderObjId, long signersObjId,
      long protectionDomainObjId, long reserved1, long reserved2, 
      int instanceSize, Constant[] constants, Static[] statics,
      InstanceField[] instanceFields) {
    
    ClassInfo cls = classMap.get(classObjId);
    if (cls == null) {
      System.err.println("Error: Could not find class " + classObjId + " from classdump");
      System.exit(1);
    }
    
    for (Static s: statics) {
      if (s.value.type == Type.OBJ) {
        System.out.println("Static, " + cls.name + ", " + stringMap.get(s.staticFieldNameStringId));
      }
    }        
  }
  
}
