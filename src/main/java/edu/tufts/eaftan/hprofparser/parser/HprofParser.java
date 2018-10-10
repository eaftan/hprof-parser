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

package edu.tufts.eaftan.hprofparser.parser;

import com.google.common.base.Preconditions;

import edu.tufts.eaftan.hprofparser.handler.RecordHandler;
import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.CPUSample;
import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Instance;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Type;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Parses an hprof heap dump file in binary format.  The hprof dump file format is documented in
 * the hprof_b_spec.h file in the hprof source, which is open-source and available from Oracle.
 */
public class HprofParser {

  private RecordHandler handler;
  private HashMap<Long, ClassInfo> classMap;

  public HprofParser(RecordHandler handler) {
    this.handler = handler;
    classMap = new HashMap<Long, ClassInfo>();
  } 

  public void parse(File file) throws IOException {

    /* The file format looks like this:
     *
     * header: 
     *   [u1]* - a null-terminated sequence of bytes representing the format
     *           name and version
     *   u4 - size of identifiers/pointers
     *   u4 - high number of word of number of milliseconds since 0:00 GMT, 
     *        1/1/70
     *   u4 - low number of word of number of milliseconds since 0:00 GMT, 
     *        1/1/70
     *
     * records:
     *   u1 - tag denoting the type of record
     *   u4 - number of microseconds since timestamp in header
     *   u4 - number of bytes that follow this field in this record
     *   [u1]* - body
     */

    FileInputStream fs = new FileInputStream(file);
    DataInputStream in = new DataInputStream(new BufferedInputStream(fs));

    // header
    String format = readUntilNull(in);
    int idSize = in.readInt();
    long startTime = in.readLong();
    handler.header(format, idSize, startTime);

    // records
    boolean done;
    do {
      done = parseRecord(in, idSize, true);
    } while (!done);
    in.close();

    FileInputStream fsSecond = new FileInputStream(file);
    DataInputStream inSecond = new DataInputStream(new BufferedInputStream(fsSecond));
    readUntilNull(inSecond); // format
    inSecond.readInt(); // idSize
    inSecond.readLong(); // startTime
    do {
      done = parseRecord(inSecond, idSize, false);
    } while (!done);
    inSecond.close();
    handler.finished();
  }

  public static String readUntilNull(DataInput in) throws IOException {

    int bytesRead = 0;
    byte[] bytes = new byte[25];

    while ((bytes[bytesRead] = in.readByte()) != 0) {
      bytesRead++;
      if (bytesRead >= bytes.length) {
        byte[] newBytes = new byte[bytesRead + 20];
        for (int i=0; i<bytes.length; i++) {
          newBytes[i] = bytes[i];
        }
        bytes = newBytes;
      }
    }
    return new String(bytes, 0, bytesRead);
  }

  /**
   * @return true if there are no more records to parse
   */
  private boolean parseRecord(DataInput in, int idSize, boolean isFirstPass) throws IOException {

    /* format:
     *   u1 - tag
     *   u4 - time
     *   u4 - length
     *   [u1]* - body
     */

    // if we get an EOF on this read, it just means we're done
    byte tag;
    try {
      tag = in.readByte();
    } catch (EOFException e) {
      return true;
    }
    
    // otherwise propagate the EOFException
    int time = in.readInt();    // TODO(eaftan): we might want time passed to handler fns
    long bytesLeft = Integer.toUnsignedLong(in.readInt());

    long l1, l2, l3, l4;
    int i1, i2, i3, i4, i5, i6, i7, i8, i9;
    short s1;
    byte b1;
    float f1;
    byte[] bArr1;
    long[] lArr1;

    switch (tag) {
      case 0x1:
        // String in UTF-8
        l1 = readId(idSize, in);
        bytesLeft -= idSize;
        bArr1 = new byte[(int) bytesLeft];
        in.readFully(bArr1);
        if (isFirstPass) {
          handler.stringInUTF8(l1, new String(bArr1));
        }
        break;

      case 0x2:
        // Load class
        i1 = in.readInt();
        l1 = readId(idSize, in);
        i2 = in.readInt();
        l2 = readId(idSize, in);
        if (isFirstPass) {
          handler.loadClass(i1, l1, i2, l2);
        }
        break;

      case 0x3:
        // Unload class
        i1 = in.readInt();
        if (isFirstPass) {
          handler.unloadClass(i1);
        }
        break;

      case 0x4:
        // Stack frame
        l1 = readId(idSize, in);
        l2 = readId(idSize, in);
        l3 = readId(idSize, in);
        l4 = readId(idSize, in);
        i1 = in.readInt();
        i2 = in.readInt();
        if (isFirstPass) {
          handler.stackFrame(l1, l2, l3, l4, i1, i2);
        }
        break;

      case 0x5:
        // Stack trace
        i1 = in.readInt();
        i2 = in.readInt();
        i3 = in.readInt();
        bytesLeft -= 12;
        lArr1 = new long[(int) bytesLeft/idSize];
        for (int i=0; i<lArr1.length; i++) {
          lArr1[i] = readId(idSize, in);
        }
        if (isFirstPass) {
          handler.stackTrace(i1, i2, i3, lArr1);
        }
        break;

      case 0x6:
        // Alloc sites
        s1 = in.readShort();
        f1 = in.readFloat();
        i1 = in.readInt();
        i2 = in.readInt();
        l1 = in.readLong();
        l2 = in.readLong();
        i3 = in.readInt();    // num of sites that follow

        AllocSite[] allocSites = new AllocSite[i3];
        for (int i=0; i<allocSites.length; i++) {
          b1 = in.readByte();
          i4 = in.readInt();
          i5 = in.readInt();
          i6 = in.readInt();
          i7 = in.readInt();
          i8 = in.readInt();
          i9 = in.readInt();

          allocSites[i] = new AllocSite(b1, i4, i5, i6, i7, i8, i9);
        }
        if (isFirstPass) {
          handler.allocSites(s1, f1, i1, i2, l1, l2, allocSites);
        }
        break;

      case 0x7: 
        // Heap summary
        i1 = in.readInt();
        i2 = in.readInt();
        l1 = in.readLong();
        l2 = in.readLong();
        if (!isFirstPass) {
          handler.heapSummary(i1, i2, l1, l2);
        }
        break;

      case 0xa:
        // Start thread
        i1 = in.readInt();
        l1 = readId(idSize, in);
        i2 = in.readInt();
        l2 = readId(idSize, in);
        l3 = readId(idSize, in);
        l4 = readId(idSize, in);
        if (isFirstPass) {
          handler.startThread(i1, l1, i2, l2, l3, l4);
        }
        break;

      case 0xb:
        // End thread
        i1 = in.readInt();
        if (isFirstPass) {
          handler.endThread(i1);
        }
        break;

      case 0xc:
        // Heap dump
        if (isFirstPass) {
          handler.heapDump();
        }
        while (bytesLeft > 0) {
          bytesLeft -= parseHeapDump(in, idSize, isFirstPass);
        }
        if (!isFirstPass) {
          handler.heapDumpEnd();
        }
        break;

      case 0x1c:
        // Heap dump segment
        if (isFirstPass) {
          handler.heapDumpSegment();
        }
        while (bytesLeft > 0) {
          bytesLeft -= parseHeapDump(in, idSize, isFirstPass);
        }
        break;

      case 0x2c:
        // Heap dump end (of segments)
        if (!isFirstPass) {
          handler.heapDumpEnd();
        }
        break;

      case 0xd:
        // CPU samples
        i1 = in.readInt();
        i2 = in.readInt();    // num samples that follow

        CPUSample[] samples = new CPUSample[i2];
        for (int i=0; i<samples.length; i++) {
          i3 = in.readInt();
          i4 = in.readInt();
          samples[i] = new CPUSample(i3, i4);
        }
        if (isFirstPass) {
          handler.cpuSamples(i1, samples);
        }
        break;

      case 0xe: 
        // Control settings
        i1 = in.readInt();
        s1 = in.readShort();
        if (isFirstPass) {
          handler.controlSettings(i1, s1);
        }
        break;

      default:
        throw new HprofParserException("Unexpected top-level record type: " + tag);
    }
    
    return false;
  }

  // returns number of bytes parsed
  private int parseHeapDump(DataInput in, int idSize, boolean isFirstPass) throws IOException {

    byte tag = in.readByte();
    int bytesRead = 1;

    long l1, l2, l3, l4, l5, l6, l7;
    int i1, i2;
    short s1, s2, s3;
    byte b1;
    byte[] bArr1;
    long [] lArr1;

    switch (tag) {

      case -1:    // 0xFF
        // Root unknown
        l1 = readId(idSize, in);
        if (isFirstPass) {
          handler.rootUnknown(l1);
        }
        bytesRead += idSize;
        break;

      case 0x01:
        // Root JNI global
        l1 = readId(idSize, in);
        l2 = readId(idSize, in);
        if (isFirstPass) {
          handler.rootJNIGlobal(l1, l2);
        }
        bytesRead += 2 * idSize;
        break;

      case 0x02:
        // Root JNI local
        l1 = readId(idSize, in);
        i1 = in.readInt();
        i2 = in.readInt();
        if (isFirstPass) {
          handler.rootJNILocal(l1, i1, i2);
        }
        bytesRead += idSize + 8;
        break;

      case 0x03:
        // Root Java frame
        l1 = readId(idSize, in);
        i1 = in.readInt();
        i2 = in.readInt();
        if (isFirstPass) {
          handler.rootJavaFrame(l1, i1, i2);
        }
        bytesRead += idSize + 8;
        break;

      case 0x04:
        // Root native stack
        l1 = readId(idSize, in);
        i1 = in.readInt();
        if (isFirstPass) {
          handler.rootNativeStack(l1, i1);
        }
        bytesRead += idSize + 4;
        break;

      case 0x05:
        // Root sticky class
        l1 = readId(idSize, in);
        if (isFirstPass) {
          handler.rootStickyClass(l1);
        }
        bytesRead += idSize;
        break;

      case 0x06:
        // Root thread block
        l1 = readId(idSize, in);
        i1 = in.readInt();
        if (isFirstPass) {
          handler.rootThreadBlock(l1, i1);
        }
        bytesRead += idSize + 4;
        break;
        
      case 0x07:
        // Root monitor used
        l1 = readId(idSize, in);
        if (isFirstPass) {
          handler.rootMonitorUsed(l1);
        }
        bytesRead += idSize;
        break;

      case 0x08:
        // Root thread object
        l1 = readId(idSize, in);
        i1 = in.readInt();
        i2 = in.readInt();
        if (isFirstPass) {
          handler.rootThreadObj(l1, i1, i2);
        }
        bytesRead += idSize + 8;
        break;

      case 0x20:
        // Class dump
        l1 = readId(idSize, in);
        i1 = in.readInt();
        l2 = readId(idSize, in);
        l3 = readId(idSize, in);
        l4 = readId(idSize, in);
        l5 = readId(idSize, in);
        l6 = readId(idSize, in);
        l7 = readId(idSize, in);
        i2 = in.readInt();
        bytesRead += idSize * 7 + 8;
        
        /* Constants */
        s1 = in.readShort();    // number of constants
        bytesRead += 2;
        Preconditions.checkState(s1 >= 0);
        Constant[] constants = new Constant[s1];
        for (int i=0; i<s1; i++) {
          short constantPoolIndex = in.readShort();
          byte btype = in.readByte();
          bytesRead += 3;
          Type type = Type.hprofTypeToEnum(btype);
          Value<?> v = null;

          switch (type) {
            case OBJ:
              long vid = readId(idSize, in);
              bytesRead += idSize;
              v = new Value<>(type, vid);
              break;
            case BOOL:
              boolean vbool = in.readBoolean();
              bytesRead += 1;
              v = new Value<>(type, vbool);
              break;
            case CHAR:
              char vc = in.readChar();
              bytesRead += 2;
              v = new Value<>(type, vc);
              break;
            case FLOAT:
              float vf = in.readFloat();
              bytesRead += 4;
              v = new Value<>(type, vf);
              break;
            case DOUBLE:
              double vd = in.readDouble();
              bytesRead += 8;
              v = new Value<>(type, vd);
              break;
            case BYTE:
              byte vbyte = in.readByte();
              bytesRead += 1;
              v = new Value<>(type, vbyte);
              break;
            case SHORT:
              short vs = in.readShort();
              bytesRead += 2;
              v = new Value<>(type, vs);
              break;
            case INT:
              int vi = in.readInt();
              bytesRead += 4;
              v = new Value<>(type, vi);
              break;
            case LONG:
              long vl = in.readLong();
              bytesRead += 8;
              v = new Value<>(type, vl);
              break;
          }

          constants[i] = new Constant(constantPoolIndex, v);
        }

        /* Statics */
        s2 = in.readShort();    // number of static fields
        bytesRead += 2;
        Preconditions.checkState(s2 >= 0);
        Static[] statics = new Static[s2];
        for (int i=0; i<s2; i++) {
          long staticFieldNameStringId = readId(idSize, in);
          byte btype = in.readByte();
          bytesRead += idSize + 1;
          Type type = Type.hprofTypeToEnum(btype);
          Value<?> v = null;

          switch (type) {
            case OBJ:     // object
              long vid = readId(idSize, in);
              bytesRead += idSize;
              v = new Value<>(type, vid);
              break;
            case BOOL:     // boolean
              boolean vbool = in.readBoolean();
              bytesRead += 1;
              v = new Value<>(type, vbool);
              break;
            case CHAR:     // char
              char vc = in.readChar();
              bytesRead += 2;
              v = new Value<>(type, vc);
              break;
            case FLOAT:     // float
              float vf = in.readFloat();
              bytesRead += 4;
              v = new Value<>(type, vf);
              break;
            case DOUBLE:     // double
              double vd = in.readDouble();
              bytesRead += 8;
              v = new Value<>(type, vd);
              break;
            case BYTE:     // byte
              byte vbyte = in.readByte();
              bytesRead += 1;
              v = new Value<>(type, vbyte);
              break;
            case SHORT:     // short
              short vs = in.readShort();
              bytesRead += 2;
              v = new Value<>(type, vs);
              break;
            case INT:    // int
              int vi = in.readInt();
              bytesRead += 4;
              v = new Value<>(type, vi);
              break;
            case LONG:    // long
              long vl = in.readLong();
              bytesRead += 8;
              v = new Value<>(type, vl);
              break;
          }

          statics[i] = new Static(staticFieldNameStringId, v);
        }

        /* Instance fields */
        s3 = in.readShort();    // number of instance fields
        bytesRead += 2;
        Preconditions.checkState(s3 >= 0);
        InstanceField[] instanceFields = new InstanceField[s3];
        for (int i=0; i<s3; i++) {
          long fieldNameStringId = readId(idSize, in);
          byte btype = in.readByte();
          bytesRead += idSize + 1;
          Type type = Type.hprofTypeToEnum(btype);
          instanceFields[i] = new InstanceField(fieldNameStringId, type);
        }

        /**
         * We need to know the types of the values in an instance record when
         * we parse that record.  To do that we need to look up the class and 
         * its superclasses.  So we need to store class records in a hash 
         * table.
         */
        if (isFirstPass) {
          classMap.put(l1, new ClassInfo(l1, l2, i2, instanceFields));
        }
        if (isFirstPass) {
          handler.classDump(l1, i1, l2, l3, l4, l5, l6, l7, i2, constants,
            statics, instanceFields);
        }
        break;

      case 0x21:
        // Instance dump
        l1 = readId(idSize, in);
        i1 = in.readInt();
        l2 = readId(idSize, in);    // class obj id
        i2 = in.readInt();    // num of bytes that follow
        Preconditions.checkState(i2 >= 0);
        bArr1 = new byte[i2];
        in.readFully(bArr1);
        
        /** 
         * because class dump records come *after* instance dump records,
         * we don't know how to interpret the values yet.  we have to
         * record the instances and process them at the end.
         */
        if (!isFirstPass) {
          processInstance(new Instance(l1, i1, l2, bArr1), idSize);
        }

        bytesRead += idSize * 2 + 8 + i2;
        break;

      case 0x22:
        // Object array dump
        l1 = readId(idSize, in);
        i1 = in.readInt();    
        i2 = in.readInt();    // number of elements
        l2 = readId(idSize, in);

        Preconditions.checkState(i2 >= 0);
        lArr1 = new long[i2];
        for (int i=0; i<i2; i++) {
          lArr1[i] = readId(idSize, in);
        }
        if (isFirstPass) {
          handler.objArrayDump(l1, i1, l2, lArr1);
        }
        bytesRead += (2 + i2) * idSize + 8;
        break;

      case 0x23:
        // Primitive array dump
        l1 = readId(idSize, in);
        i1 = in.readInt();
        i2 = in.readInt();    // number of elements
        b1 = in.readByte();
        bytesRead += idSize + 9;

        Preconditions.checkState(i2 >= 0);
        Value<?>[] vs = new Value[i2];
        Type t = Type.hprofTypeToEnum(b1);
        for (int i=0; i<vs.length; i++) {
          switch (t) {
            case OBJ:
              long vobj = readId(idSize, in);
              vs[i] = new Value<>(t, vobj);
              bytesRead += idSize;
              break;
            case BOOL:
              boolean vbool = in.readBoolean();
              vs[i] = new Value<>(t, vbool);
              bytesRead += 1;
              break;
            case CHAR:
              char vc = in.readChar();
              vs[i] = new Value<>(t, vc);
              bytesRead += 2;
              break;
            case FLOAT:
              float vf = in.readFloat();
              vs[i] = new Value<>(t, vf);
              bytesRead += 4;
              break;
            case DOUBLE:
              double vd = in.readDouble();
              vs[i] = new Value<>(t, vd);
              bytesRead += 8;
              break;
            case BYTE:
              byte vbyte = in.readByte();
              vs[i] = new Value<>(t, vbyte);
              bytesRead += 1;
              break;
            case SHORT:
              short vshort = in.readShort();
              vs[i] = new Value<>(t, vshort);
              bytesRead += 2;
              break;
            case INT:
              int vi = in.readInt();
              vs[i] = new Value<>(t, vi);
              bytesRead += 4;
              break;
            case LONG:
              long vlong = in.readLong();
              vs[i] = new Value<>(t, vlong);
              bytesRead += 8;
              break;
          }
        } 
        if (isFirstPass) {
          handler.primArrayDump(l1, i1, b1, vs);
        }
        break;

      default:
        throw new HprofParserException("Unexpected heap dump sub-record type: " + tag);
    }

    return bytesRead;
    
  }

  private void processInstance(Instance i, int idSize) throws IOException {
    ByteArrayInputStream bs = new ByteArrayInputStream(i.packedValues);
    DataInputStream input = new DataInputStream(bs);

    ArrayList<Value<?>> values = new ArrayList<>();

    // superclass of Object is 0
    long nextClass = i.classObjId;
    while (nextClass != 0) {
      ClassInfo ci = classMap.get(nextClass);
      nextClass = ci.superClassObjId;
      for (InstanceField field : ci.instanceFields) {
        Value<?> v = null;
        switch (field.type) {
          case OBJ:     // object
            long vid = readId(idSize, input);
            v = new Value<>(field.type, vid);
            break;
          case BOOL:     // boolean
            boolean vbool = input.readBoolean();
            v = new Value<>(field.type, vbool);
            break;
          case CHAR:     // char
            char vc = input.readChar();
            v = new Value<>(field.type, vc);
            break;
          case FLOAT:     // float
            float vf = input.readFloat();
            v = new Value<>(field.type, vf);
            break;
          case DOUBLE:     // double
            double vd = input.readDouble();
            v = new Value<>(field.type, vd);
            break;
          case BYTE:     // byte
            byte vbyte = input.readByte();
            v = new Value<>(field.type, vbyte);
            break;
          case SHORT:     // short
            short vs = input.readShort();
            v = new Value<>(field.type, vs);
            break;
          case INT:    // int
            int vi = input.readInt();
            v = new Value<>(field.type, vi);
            break;
          case LONG:    // long
            long vl = input.readLong();
            v = new Value<>(field.type, vl);
            break;
        }
        values.add(v);
      }
    }
    Value<?>[] valuesArr = new Value[values.size()];
    valuesArr = values.toArray(valuesArr);
    handler.instanceDump(i.objId, i.stackTraceSerialNum, i.classObjId, valuesArr);
  }

  private static long readId(int idSize, DataInput in) throws IOException {
    long id = -1;
    if (idSize == 4) {
      id = in.readInt();
      id &= 0x00000000ffffffff;     // undo sign extension
    } else if (idSize == 8) {
      id = in.readLong();
    } else {
      throw new IllegalArgumentException("Invalid identifier size " + idSize);
    }

    return id;
  }

 
  /* Utility */

  private int mySkipBytes(int n, DataInput in) throws IOException {
    int bytesRead = 0;
    
    try {
      while (bytesRead < n) {
        in.readByte();
        bytesRead++;
      }
    } catch (EOFException e) {
      // expected
    }
    
    return bytesRead;
  }


}

