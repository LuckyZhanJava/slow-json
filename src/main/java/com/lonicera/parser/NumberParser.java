package com.lonicera.parser;

import com.lonicera.environment.Environment;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author LiBowei
 * @date 2021年-06月-21日
 **/
public class NumberParser implements Parser {
  private static BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);
  private static BigDecimal MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);
  private static BigDecimal MIN_FLOAT = BigDecimal.valueOf(-Float.MAX_VALUE);
  private static BigDecimal MAX_INT = BigDecimal.valueOf(Integer.MAX_VALUE);
  private static BigDecimal MIN_INT = BigDecimal.valueOf(Integer.MIN_VALUE);
  private static BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
  private static BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);

  @Override
  public ASTNode parse(Lexer lexer) {
    if (match(lexer)) {
      final Token token = lexer.read();
      return new ASTNode() {

        @Override
        public Object eval(Environment env) {
          String text = token.text();
          BigDecimal value = new BigDecimal(text);
          if(value.compareTo(MAX_LONG) > 0 || value.compareTo(MIN_LONG) < 0){
            return value;
          }
          if (text.indexOf('.') > 0) {
            if(inFloatRange(value)){
              return Float.parseFloat(text);
            }else {
              return Double.parseDouble(text);
            }
          }
          if(inIntRange(value)){
            return Integer.parseInt(text);
          }else {
            return Long.parseLong(text);
          }
        }

        private boolean inIntRange(BigDecimal value) {
          return value.compareTo(MAX_INT) < 0 && value.compareTo(MIN_INT) > 0;
        }

        private boolean inFloatRange(BigDecimal value) {
          return value.compareTo(MAX_FLOAT) < 0 && value.compareTo(MIN_FLOAT) > 0;
        }
      };
    } else {
      throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "0-9");
    }
  }


  @Override
  public boolean match(Lexer lexer) {
    Token token = lexer.peek();
    return token.getClass().equals(NumberToken.class);
  }
}
