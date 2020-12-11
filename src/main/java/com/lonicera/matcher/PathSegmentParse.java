package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.parse.Parse;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class PathSegmentParse extends Parse {

  @Override
  protected boolean match(Lexer lexer) {
    Token token = lexer.lookAhead(1);
    Class<? extends Token> tokenClass = token.getClass();
    return (tokenClass.equals(PathSegmentToken.class));
  }

  @Override
  public Token[] parse(Lexer lexer) {
    return new Token[]{lexer.nextToken()};
  }
}
