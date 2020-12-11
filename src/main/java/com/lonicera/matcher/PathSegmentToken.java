package com.lonicera.matcher;

import com.lonicera.token.AbstractToken;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class PathSegmentToken extends AbstractToken {
  private String path;

  public PathSegmentToken(String path) {
    this.path = path;
  }

  @Override
  public String text() {
    return path;
  }
}
