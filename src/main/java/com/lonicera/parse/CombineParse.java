package com.lonicera.parse;

import com.lonicera.lexer.Lexer;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class CombineParse extends Parse{
  private Parse[] parses;

  private CombineParse(Parse... parses){
    this.parses = parses;
  }

  public static CombineParse of(Parse... parses){
    return new CombineParse(parses);
  }

  @Override
  protected boolean match(Lexer lexer) {
    for(Parse parse : parses){
      if(!parse.match(lexer)){
        return false;
      }
    }
    return true;
  }

  @Override
  public Token[] parse(Lexer lexer) {
    return new Token[0];
  }
}
