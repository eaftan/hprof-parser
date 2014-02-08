package edu.tufts.eaftan.hprofparser.parser;

/**
 * Thrown when the parser doesn't understand something about the the heap dump.  Since there is 
 * no reasonable way to recover from these, they are unchecked.  
 */
public class HprofParserException extends RuntimeException {
  public HprofParserException(String message) {
    super(message);
  }
}
