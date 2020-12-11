package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.parse.Parse;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class SkipParse implements Parse {
  private SkipToken skipToken;

  private SkipParse(SkipToken skipToken){
    this.skipToken = skipToken;
  }

  public static SkipParse of(SkipToken skipToken){
    return new SkipParse(skipToken);
  }

  @Override
  public boolean match(Lexer lexer) {
    Token nextToken = lexer.lookAhead(1);
    Class<? extends Token> nextTokenType = nextToken.getClass();
    return (nextTokenType.equals(SkipToken.class) && nextToken.text().equals(skipToken.text()));
  }

  @Override
  public Token[] parse(Lexer lexer) {
    return new Token[0];
  }
}
