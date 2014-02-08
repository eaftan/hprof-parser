package edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler;


/**
 * Simple data object that records basic information about a class.
 */
public class ClassInfo extends TypeInfo {
  public long instanceSize = -1;
    
  @Override
  public long totalSize() {
    return instanceSize * instanceCount;
  }
}
