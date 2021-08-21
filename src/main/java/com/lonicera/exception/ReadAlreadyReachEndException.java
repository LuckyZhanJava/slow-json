package com.lonicera.exception;

public class ReadAlreadyReachEndException extends RuntimeException {

  public ReadAlreadyReachEndException() {

  }

  @Override
  public String toString() {
    return "Lexer Read Already Reach End !";
  }
}
