package com.lonicera.matcher;

import com.lonicera.token.AbstractToken;

/**
 * @author LiBowei
 * @date 2020年-12月-07日
 **/
public class SkipToken extends AbstractToken {
  private String skip;

  public SkipToken(char skip){
    this.skip = String.valueOf(skip);
  }

  public SkipToken(String skip){
    this.skip = skip;
  }

  @Override
  public String text() {
    return skip;
  }
}
