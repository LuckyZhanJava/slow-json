package com.lonicera.parser;

import com.lonicera.environment.Environment;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.StringToken;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2021年-06月-21日
 **/
public class StringParser implements Parser {

  @Override
  public ASTNode parse(Lexer lexer) {
    if(match(lexer)){
      StringToken token = (StringToken)lexer.read();
      return new ASTNode(){

        @Override
        public Object eval(Environment env) {
          return token.text();
        }

      };
    }else{
      Token next = lexer.peek();
      throw new UnExpectTokenException(lexer.expr(), next, StringToken.class);
    }
  }

  @Override
  public boolean match(Lexer lexer) {
    Token token = lexer.peek();
    return token.getClass().equals(StringToken.class);
  }
}
