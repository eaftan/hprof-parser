package edu.tufts.eaftan.hprofparser.parser;

/**
 * Thrown when the parser encounters a top-level record type it doesn't understand.
 */
public class UnexpectedRecordTypeException extends HprofParserException {

  /**
   * @param tag The tag of the record the parser didn't understand
   */
  public UnexpectedRecordTypeException(byte tag) {
    super("Unexpected top-level record type: " + tag);
  }

}
