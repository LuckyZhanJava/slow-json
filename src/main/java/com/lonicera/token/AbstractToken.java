package com.lonicera.token;

public abstract class AbstractToken<T extends AbstractToken<T>> implements Token {

  private char[] chars;
  private int offset;
  private int length;

  protected AbstractToken(){
  }

  public AbstractToken(char[] chars, int offset, int length) {
    this.chars = chars;
    this.offset = offset;
    this.length = length;
  }

  public T reset(char[] chars, int offset, int length){
    this.chars = chars;
    this.offset = offset;
    this.length = length;
    return (T) this;
  }

  @Override
  public int offset() {
    return offset;
  }

  public char[] chars() {
    return chars;
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public String text() {
    return new String(chars, offset, length);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " : " + text();
  }
}
