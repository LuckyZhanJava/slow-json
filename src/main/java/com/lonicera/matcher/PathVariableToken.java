package com.lonicera.matcher;

import com.lonicera.token.AbstractToken;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class PathVariableToken extends AbstractToken {
  private String variableName;

  public PathVariableToken(String path) {
    this.variableName = path;
  }
  
  public String getVariableName() {
	  return variableName;
  }
  
  @Override
  public String text() {
    return '{' + variableName + '}';
  }
}
