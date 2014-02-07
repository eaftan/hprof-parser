package edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler;

/**
 * Simple data object that records basic information about an array type.
 */
public class ArrayInfo extends TypeInfo {
  public long totalSize = 0;
  
  @Override
  public long totalSize() {
    return totalSize;
  }
}
