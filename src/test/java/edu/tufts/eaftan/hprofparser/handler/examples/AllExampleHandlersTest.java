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

import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Test;

import edu.tufts.eaftan.hprofparser.handler.NullRecordHandler;

import edu.tufts.eaftan.hprofparser.handler.RecordHandler;
import edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler.StatisticsCollectingHandler;
import edu.tufts.eaftan.hprofparser.parser.HprofParser;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Runs all example handlers on the test file and ensures they don't crash.
 */
public class AllExampleHandlersTest {
  
  private static final List<RecordHandler> ALL_HANDLERS = ImmutableList.<RecordHandler>of(
      new NullRecordHandler(),
      new PrintHandler(),
      new RootHandler(),
      new StaticPrintHandler(),
      new StatisticsCollectingHandler());
    
  private static String getAbsolutePathForResource(String relativePath) throws URISyntaxException {
    return new File(ClassLoader.getSystemResource(relativePath).toURI()).getAbsolutePath();
  }
  
  @Before
  public void setUp() {
    PrintStream throwawayPrintStream = new PrintStream(new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        // do nothing
      }
    });
    System.setOut(throwawayPrintStream);
    System.setErr(throwawayPrintStream);
  }

  @Test
  public void testAllHandlers() throws Exception {
    String testFilePath = getAbsolutePathForResource("java.hprof");
    
    for (RecordHandler handler : ALL_HANDLERS) {
      HprofParser parser = new HprofParser(handler);
      parser.parse(new File(testFilePath));
    }
  }

}
