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

import edu.tufts.eaftan.hprofparser.parser.HprofParserException;

public enum Type {

  OBJ("Object", 4), 
  BOOL("boolean", 1), 
  CHAR("char", 2), 
  FLOAT("float", 4), 
  DOUBLE("double", 8), 
  BYTE("byte", 1), 
  SHORT("short", 2), 
  INT("int", 4), 
  LONG("long", 8);
  
  private final String name;
  private final int sizeInBytes;
  
  private Type(String name, int sizeInBytes) {
    this.name = name;
    this.sizeInBytes = sizeInBytes;
  }
  
  public int sizeInBytes() {
    return sizeInBytes;
  }

  public static Type hprofTypeToEnum(byte type) {
    switch (type) {
      case 2:
        return OBJ;
      case 4:
        return BOOL;
      case 5:
        return CHAR;
      case 6:
        return FLOAT;
      case 7:
        return DOUBLE;
      case 8:
        return BYTE;
      case 9:
        return SHORT;
      case 10:
        return INT;
      case 11:
        return LONG;
      default:
        throw new HprofParserException("Unexpected type in heap dump: " + type);
    }
  }
  
  @Override
  public String toString() {
    return name;
  }

}


