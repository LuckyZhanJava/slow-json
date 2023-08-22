package com.lonicera.parser;

import com.lonicera.context.EvalContext;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.serializer.TypeSerializer;
import com.lonicera.token.Lexer;
import com.lonicera.token.StringToken;
import com.lonicera.token.Token;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Lonicera
 * @date 2021年-06月-21日
 **/
public class StringParser implements Parser {

  @Override
  public ASTNode tryParse(Lexer lexer) {
    if(match(lexer)){
      return parse(lexer);
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), StringToken.class);
  }

  @Override
  public ASTNode parse(Lexer lexer) {
    StringToken token = (StringToken) lexer.read();
    return new ASTNode() {

      @Override
      public Object eval(EvalContext context, Type type, Annotation[] targetAnnotations) {
        if (type instanceof Class) {
          TypeSerializer serializer = context.getTypeSerializer((Class<?>) type);
          return serializer.deserialize(type, targetAnnotations, token.value());
        }
        return token.text();
      }
    };
  }

  @Override
  public boolean match(Lexer lexer) {
    Token token = lexer.peek();
    return token.getClass().equals(StringToken.class);
  }
}
