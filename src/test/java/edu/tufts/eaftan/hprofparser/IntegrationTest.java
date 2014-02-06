package edu.tufts.eaftan.hprofparser;

import static org.hamcrest.core.StringContains.containsString;

import org.junit.After;
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
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  private String getAbsolutePathForResource(String relativePath) throws URISyntaxException {
    return new File(ClassLoader.getSystemResource(relativePath).toURI()).getAbsolutePath();
  }
  
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }
  
  @Test
  public void basicUsage() throws Exception {
    String[] args = {getAbsolutePathForResource("java.hprof")};
    Parse.main(args);
    
    Assert.assertThat(outContent.toString(), containsString("JAVA PROFILE 1.0.1"));
  }
  
  @Test
  public void commandLineHandler() throws Exception {
    String[] args = {"--handler=edu.tufts.eaftan.hprofparser.handler.StaticPrintHandler", 
        getAbsolutePathForResource("java.hprof")};
    Parse.main(args);
    
    Assert.assertThat(outContent.toString(), containsString("Static"));
  }

}
