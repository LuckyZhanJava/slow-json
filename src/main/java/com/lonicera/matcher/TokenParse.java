package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.parse.Parse;
import com.lonicera.parse.UnexpectTokenTypeException;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class TokenParse extends Parse {
  private Token token;

  private TokenParse(Token token){
    this.token = token;
  }

  public static TokenParse of(Token token){
    return new TokenParse(token);
  }

  @Override
  protected boolean match(Lexer lexer) {
    com.lonicera.token.Token nextToken = lexer.lookAhead(1);
    Class<? extends com.lonicera.token.Token> nextTokenType = nextToken.getClass();
    return (nextTokenType.equals(Token.class) && nextToken.text().equals(token.text()));
  }

  @Override
  public com.lonicera.token.Token[] parse(Lexer lexer) {
    if(match(lexer)){
      return new com.lonicera.token.Token[0];
    }
    throw new UnexpectTokenTypeException(null, lexer.lookAhead(1).getClass());
  }
}
