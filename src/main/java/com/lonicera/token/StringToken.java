package com.lonicera.token;

public class StringToken extends AbstractToken<StringToken> {

  private String value;

  public StringToken() {

  }

  public StringToken(char[] chars, int offset, int length, String value) {
    super(chars, offset, length);
    this.value = value;
  }

  public String value() {
    return value;
  }
}
