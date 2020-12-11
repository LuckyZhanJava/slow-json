package com.lonicera.parse;


/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class UnexpectTokenTextException extends RuntimeException{
  private String expect;
  private String present;
  public UnexpectTokenTextException(String expect, String present){
    this.expect = expect;
    this.present = present;
  }

  @Override
  public String getMessage() {
    return "Expect: " + expect.toString() + ", Present: " + present.toString();
  }
}
