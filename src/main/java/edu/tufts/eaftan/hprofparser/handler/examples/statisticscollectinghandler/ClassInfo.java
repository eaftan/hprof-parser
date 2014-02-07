package edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler;

import com.google.common.base.Preconditions;

/**
 * Simple data object that records basic information about a class.
 */
public class ClassInfo extends TypeInfo {
  public long instanceSize = -1;
  
  @Override
  public void validate() {
    super.validate();
    Preconditions.checkState(instanceSize != -1);
  }
  
  @Override
  public long totalSize() {
    return instanceSize * instanceCount;
  }
}
