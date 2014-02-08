package edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler;

/**
 * Simple data object that records basic information about a type.
 */
public abstract class TypeInfo {
  
  public String className = null;
  public long instanceCount = 0;
    
  /**
   * Returns the cumulative size in bytes of all objects of this type.
   */
  public abstract long totalSize();
  
  @Override
  public String toString() {
    return String.format("%s: %d bytes, %d instances", 
        className, totalSize(), instanceCount);
  }
}
