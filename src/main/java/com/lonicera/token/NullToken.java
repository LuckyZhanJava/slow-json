package com.lonicera.token;

public class NullToken extends AbstractToken {

  public NullToken() {
    super();
  }

  public NullToken(char[] chars, int offset) {
    super(chars, offset, 4);
  }
}
