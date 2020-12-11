package com.lonicera.parse;

import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class UnexpectTokenTypeException extends RuntimeException{
  private Class<? extends Token> expect;
  private Class<? extends Token> present;
  public UnexpectTokenTypeException(Class<? extends Token> expect, Class<? extends Token> present){
    this.expect = expect;
    this.present = present;
  }

  @Override
  public String getMessage() {
    return "Expect: " + expect.toString() + ", Present: " + present.toString();
  }
}
