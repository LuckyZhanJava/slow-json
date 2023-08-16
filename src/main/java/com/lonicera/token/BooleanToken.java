package com.lonicera.token;

public class BooleanToken extends AbstractToken {

  private boolean value;

  public BooleanToken(){
    super();
  }

  public BooleanToken(char[] chars, int offset, int length) {
    super(chars, offset, length);
  }

  public boolean value() {
    return length() == 4;
  }
}
