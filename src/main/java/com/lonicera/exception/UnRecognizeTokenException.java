package com.lonicera.exception;

public class UnRecognizeTokenException extends RuntimeException {

  private String token;

  public UnRecognizeTokenException(char[] chars, int offset, int length) {
    this.token = new String(chars, offset, length);
  }

  @Override
  public String toString() {
    return "UnRecognize Token : " + token +
        "←";
  }
}
