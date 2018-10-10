package edu.tufts.eaftan.hprofparser.handler;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import edu.tufts.eaftan.hprofparser.parser.HprofParser;
import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.CPUSample;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Tests that callback methods are never called with a null argument.
 */
public class NonNullTest {
  
  private static final RecordHandler NULL_CHECKER_HANDLER = new NullRecordHandler() {
    @Override
    public void stringInUTF8(long id, String data) {
      assertThat(data, notNullValue());
    }

    @Override
    public void stackTrace(int stackTraceSerialNum, int threadSerialNum, int numFrames,
        long[] stackFrameIds) {
      assertThat(stackFrameIds, notNullValue());
    }

    @Override
    public void allocSites(short bitMaskFlags,
        float cutoffRatio,
        int totalLiveBytes,
        int totalLiveInstances,
        long totalBytesAllocated,
        long totalInstancesAllocated,
        AllocSite[] sites) {
      assertThat(sites, notNullValue());
    }

    @Override
    public void cpuSamples(int totalNumOfSamples, CPUSample[] samples) {
      assertThat(samples, notNullValue());
    }

    @Override
    public void classDump(long classObjId,
        int stackTraceSerialNum,
        long superClassObjId,
        long classLoaderObjId,
        long signersObjId,
        long protectionDomainObjId,
        long reserved1,
        long reserved2,
        int instanceSize,
        Constant[] constants,
        Static[] statics,
        InstanceField[] instanceFields) {
      assertThat(constants, notNullValue());
      assertThat(statics, notNullValue());
      assertThat(instanceFields, notNullValue());
    }

    @Override
    public void instanceDump(long objId, int stackTraceSerialNum, long classObjId,
        Value<?>[] instanceFieldValues) {
      assertThat(instanceFieldValues, notNullValue());
    }

    @Override
    public void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId,
        long[] elems) {
      assertThat(elems, notNullValue());
    }

    @Override
    public void primArrayDump(long objId, int stackTraceSerialNum, byte elemType, Value<?>[] elems) {
      assertThat(elems, notNullValue());
    }    
  };
  
  private static String getAbsolutePathForResource(String relativePath) throws URISyntaxException {
    return new File(ClassLoader.getSystemResource(relativePath).toURI()).getAbsolutePath();
  }
  
  @Test
  public void argumentsMustBeNonNull() throws Exception {
    String testFilePath = getAbsolutePathForResource("java.hprof");
    HprofParser parser = new HprofParser(NULL_CHECKER_HANDLER);
    parser.parse(new File(testFilePath));
  }

}
