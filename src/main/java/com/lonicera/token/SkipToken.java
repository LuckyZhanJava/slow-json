package com.lonicera.token;

public class SkipToken extends AbstractToken {

    public SkipToken(){
        super();
    }

  public SkipToken(char[] chars, int offset) {
    super(chars, offset, 1);
  }

  public char value() {
    return chars()[offset()];
  }

  public String toString() {
    return getClass().getSimpleName() + " : " + text();
  }
}
