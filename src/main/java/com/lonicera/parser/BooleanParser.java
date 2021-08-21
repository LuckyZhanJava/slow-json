package com.lonicera.parser;

import com.lonicera.environment.Environment;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.BooleanToken;
import com.lonicera.token.Lexer;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2021年-06月-21日
 **/
public class BooleanParser implements Parser {

  @Override
  public ASTNode parse(Lexer lexer) {
    if (match(lexer)) {
      final BooleanToken token = (BooleanToken) lexer.read();
      return new ASTNode() {

        @Override
        public Object eval(Environment env) {
          String text = token.text();
          return text.equals("true");
        }

      };
    } else {
      throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "true Or false");
    }
  }

  @Override
  public boolean match(Lexer lexer) {
    Token token = lexer.peek();
    return token.getClass().equals(BooleanToken.class);
  }
}
