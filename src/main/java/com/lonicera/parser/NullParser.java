package com.lonicera.parser;

import com.lonicera.environment.Environment;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.NullToken;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2021年-06月-21日
 **/
public class NullParser implements Parser {

  @Override
  public ASTNode parse(Lexer lexer) {
    if (match(lexer)) {
      final NullToken token = (NullToken) lexer.read();
      return new ASTNode() {
        @Override
        public Object eval(Environment env) {
          return null;
        }
      };
    } else {
      throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "null");
    }
  }

  @Override
  public boolean match(Lexer lexer) {
    Token token = lexer.peek();
    return token.getClass().equals(NullToken.class);
  }
}
