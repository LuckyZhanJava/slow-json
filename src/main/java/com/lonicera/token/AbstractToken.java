package com.lonicera.token;

/**
 * @author LiBowei
 * @date 2020年-12月-07日
 **/
public abstract class AbstractToken implements Token{
  @Override
  public String toString(){
    return getClass() + ":[" + text() + "]";
  }
}
