package com.lonicera.parse;

import com.lonicera.token.Token;
import com.lonicera.lexer.Lexer;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class MatchParse extends Parse{
  private Class<? extends Token> tokenClass;

  private MatchParse(Class<? extends Token> tokenClass){
    this.tokenClass = tokenClass;
  }

  public static MatchParse of(Class<? extends Token> tokenClass){
    return new MatchParse(tokenClass);
  }

  @Override
  protected boolean match(Lexer lexer) {
    Token next = lexer.nextToken();
    Class<? extends Token> nextType = next.getClass();
    if(nextType.equals(tokenClass)){
      return true;
    }
    return false;
  }

  @Override
  public Token[] parse(Lexer lexer) {
    return new Token[0];
  }
}
