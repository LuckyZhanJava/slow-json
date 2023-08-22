package com.lonicera.parser;

import com.lonicera.context.EvalContext;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.serializer.TypeSerializer;
import com.lonicera.token.Lexer;
import com.lonicera.token.NumberToken;
import com.lonicera.token.Token;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Lonicera
 * @date 2021年-06月-21日
 **/
public class NumberParser implements Parser {

  @Override
  public ASTNode tryParse(Lexer lexer) {
    if (match(lexer)) {
      return parse(lexer);
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), NumberToken.class);
  }

  @Override
  public ASTNode parse(Lexer lexer) {
    final Token token = lexer.read();
    return new ASTNode() {

      @Override
      public Object eval(EvalContext context, Type targetType, Annotation[] targetAnnotations) {
        String text = token.text();
        TypeSerializer typeSerializer = context.getTypeSerializer((Class<?>) targetType);
        return typeSerializer.deserialize(targetType, targetAnnotations, text);
      }
    };
  }


  @Override
  public boolean match(Lexer lexer) {
    Token token = lexer.peek();
    return token.getClass().equals(NumberToken.class);
  }
}
