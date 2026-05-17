package project.exceptions;

public class TooManyFailuresException extends Exception {
  public TooManyFailuresException(String message) {
    super(message);
  }
}