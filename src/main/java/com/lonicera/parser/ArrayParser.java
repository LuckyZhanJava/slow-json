package com.lonicera.parser;

import com.lonicera.context.EvalContext;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.SkipToken;
import com.lonicera.token.Token;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ArrayParser implements Parser {

  private Parser elementParser;

  public ArrayParser() {
  }

  public Parser elementParser(Parser elementParser) {
    this.elementParser = elementParser;
    return this;
  }


  @Override
  public ASTNode tryParse(Lexer lexer) {
    if(match(lexer)){
      return parse(lexer);
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "[");
  }

  @Override
  public ASTNode parse(Lexer lexer) {
    lexer.read();
    final List<ASTNode> children = new LinkedList<>();
    if (!isSkipToken(lexer.peek(), ']')) {
      children.add(elementParser.tryParse(lexer));
    }
    while (!isSkipToken(lexer.peek(), ']')) {
      Token next = lexer.read();
      if (!isSkipToken(next, ',')) {
        throw new UnExpectTokenException(lexer.expr(), next, ",");
      }
      ASTNode astNode = elementParser.tryParse(lexer);
      children.add(astNode);
    }
    lexer.read();
    return new ASTNode() {
      @Override
      public Object eval(EvalContext context, Type targetType, Annotation[] targetAnnotations) {
        Class<?> elementType;
        Class<?> targetClass;
        if (targetType instanceof Class) {
          if (((Class) targetType).isArray()) {
            targetClass = (Class<?>) targetType;
            elementType = targetClass.getComponentType();
          } else {
            targetClass = (Class<?>) targetType;
            elementType = Object.class;
          }
        } else if (targetType instanceof ParameterizedType) {
          targetClass = (Class<?>) ((ParameterizedType) targetType).getRawType();
          elementType = (Class<?>) ((ParameterizedType) targetType).getActualTypeArguments()[0];
        } else {
          targetClass = null;
          elementType = null;
        }
        Object[] objects = new Object[children.size()];
        for (int i = 0; i < children.size(); i++) {
          objects[i] = children.get(i).eval(context, elementType, targetAnnotations);
        }
        return context.getTypeSerializer(targetClass)
            .deserialize(targetType, targetAnnotations, objects);
      }
    };
  }

  @Override
  public boolean match(Lexer lexer) {
    return isSkipToken(lexer.peek(), '[');
  }

  private boolean isSkipToken(Token next, char expectText) {
    return next instanceof SkipToken && ((SkipToken) next).value() == expectText;
  }
}
