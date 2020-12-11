package com.lonicera.matcher;

import com.lonicera.token.AbstractToken;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class PathSeparateToken extends AbstractToken {
  private String path;

  public PathSeparateToken() {
    this.path = "/";
  }

  @Override
  public String text() {
    return path;
  }
}
