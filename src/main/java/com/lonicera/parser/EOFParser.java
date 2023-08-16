package com.lonicera.parser;

import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2021年-06月-25日
 **/
public class EOFParser implements Parser {

  @Override
  public ASTNode tryParse(Lexer lexer) {
    if (match(lexer)) {
      return parse(lexer);
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "<EOF>");
  }

  @Override
  public ASTNode parse(Lexer lexer) {
    return null;
  }

  @Override
  public boolean match(Lexer lexer) {
    return lexer.peek() == Token.EOF;
  }
}
