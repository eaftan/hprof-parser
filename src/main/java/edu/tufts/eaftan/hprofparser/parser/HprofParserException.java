package edu.tufts.eaftan.hprofparser.parser;

/**
 * Superclass for all hprof parser exceptions. These are thrown when the parser doesn't 
 * understand the heap dump.  Since there is no reasonable way to recover from these, they are
 * unchecked.  
 */
public abstract class HprofParserException extends RuntimeException {
  public HprofParserException(String message) {
    super(message);
  }
}
