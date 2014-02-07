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

package edu.tufts.eaftan.hprofparser;

import static org.hamcrest.core.StringContains.containsString;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;

/**
 * End-to-end test of the parser.
 */
public class IntegrationTest {
  
  private static String getAbsolutePathForResource(String relativePath) throws URISyntaxException {
    return new File(ClassLoader.getSystemResource(relativePath).toURI()).getAbsolutePath();
  }
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @Test
  public void basicUsage() throws Exception {
    String[] args = {getAbsolutePathForResource("java.hprof")};
    Parse.main(args);
    
    Assert.assertThat(outContent.toString(), containsString("JAVA PROFILE 1.0.1"));
  }
  
  @Test
  public void commandLineHandler() throws Exception {
    String[] args = {"--handler=edu.tufts.eaftan.hprofparser.handler.examples.StaticPrintHandler", 
        getAbsolutePathForResource("java.hprof")};
    Parse.main(args);
    
    Assert.assertThat(outContent.toString(), containsString("Static"));
  }

}
