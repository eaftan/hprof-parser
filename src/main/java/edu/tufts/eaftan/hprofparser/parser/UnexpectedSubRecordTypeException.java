package edu.tufts.eaftan.hprofparser.parser;

/**
 * Thrown when the parser encounters a heap dump sub-record type it doesn't understand.
 */
public class UnexpectedSubRecordTypeException extends HprofParserException {

  /**
   * @param tag The tag of the heap dump sub-record the parser didn't understand
   */
  public UnexpectedSubRecordTypeException(byte tag) {
    super("Unexpected heap dump sub-record type: " + tag);
  }

}
