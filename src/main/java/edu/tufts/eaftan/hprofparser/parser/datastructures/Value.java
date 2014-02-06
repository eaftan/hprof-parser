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

/**
 * This class represents a value from the Hprof file.  The value can be of many possible types.
 */
public class Value {

  public Type type;

  private long objVal;
  private boolean boolVal;
  private char charVal;
  private float floatVal;
  private double doubleVal;
  private byte byteVal;
  private short shortVal;
  private int intVal;
  private long longVal;

  private boolean initialized = false;


  // This constructor has to work for both object and long types since we use
  // longs to represent both
  public Value(Type type, long objVal) {

    switch (type) {
      case OBJ:
        this.type = type;
        this.objVal = objVal;
        initialized = true;
        break;

      case LONG:
        this.type = type;
        this.longVal = objVal;
        initialized = true;
        break;

      default:
        System.err.println("Error: wrong type in Value");
        System.exit(1);
        break;

    }

  }

  /* Constructors */

  public Value(Type type, boolean boolVal) {
    if (type != Type.BOOL) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.boolVal = boolVal;
    initialized = true;
  }
  
  public Value(Type type, char charVal) {
    if (type != Type.CHAR) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.charVal = charVal;
    initialized = true;
  }

  public Value(Type type, float floatVal) {
    if (type != Type.FLOAT) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.floatVal = floatVal;
    initialized = true;
  }
  
  public Value(Type type, double doubleVal) {
    if (type != Type.DOUBLE) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.doubleVal = doubleVal;
    initialized = true;
  }
  
  public Value(Type type, byte byteVal) {
    if (type != Type.BYTE) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.byteVal = byteVal;
    initialized = true;
  }
  
  public Value(Type type, short shortVal) {
    if (type != Type.SHORT) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.shortVal = shortVal;
    initialized = true;
  }
  
  public Value(Type type, int intVal) {
    if (type != Type.INT) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    this.type = type;
    this.intVal = intVal;
    initialized = true;
  }

  /* Getter methods */
  public long getValueObj() {
    if (type != Type.OBJ) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return objVal;
  }

  public boolean getValueBool() {
    if (type != Type.BOOL) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return boolVal;
  }

  public char getValueChar() {
    if (type != Type.CHAR) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return charVal;
  }

  public float getValueFloat() {
    if (type != Type.FLOAT) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return floatVal;
  }

  public double getValueDouble() {
    if (type != Type.DOUBLE) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return doubleVal;
  }

  public byte getValueByte() {
    if (type != Type.BYTE) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return byteVal;
  }

  public short getValueShort() {
    if (type != Type.SHORT) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return shortVal;
  }

  public int getValueInt() {
    if (type != Type.INT) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return intVal;
  }

  public long getValueLong() {
    if (type != Type.LONG) {
      System.err.println("Error: wrong type in Value");
      System.exit(1);
    }
    if (!initialized) {
      System.err.println("Error: attempt to read from uninitialized value field");
      System.exit(1);
    }

    return longVal;
  }

  @Override
  public String toString() {
    if (!initialized)
      System.err.println("Error: cannot print uninitialized value");

    switch (type) {
      case OBJ:
        return Long.toString(objVal);
      case BOOL:
        return Boolean.toString(boolVal);
      case CHAR:
        return Character.toString(charVal);
      case FLOAT:
        return Float.toString(floatVal);
      case DOUBLE:
        return Double.toString(doubleVal);
      case BYTE:
        return Byte.toString(byteVal);
      case SHORT:
        return Short.toString(shortVal);
      case INT:
        return Integer.toString(intVal);
      case LONG:
        return Long.toString(longVal);
    }

    return null;

  }

}
