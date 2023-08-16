package com.lonicera.parser;

import com.lonicera.context.EvalContext;
import com.lonicera.exception.ExprEvalException;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.reflect.Reflector;
import com.lonicera.reflect.Reflector.PropertyMeta;
import com.lonicera.reflect.ReflectorFactory;
import com.lonicera.token.Lexer;
import com.lonicera.token.SkipToken;
import com.lonicera.token.Token;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectParser implements Parser {

  private Parser stringParser = new StringParser();
  private Parser valueParser;

  public ObjectParser() {

  }

  public ObjectParser valueParser(Parser valueParser) {
    this.valueParser = valueParser;
    return this;
  }

  @Override
  public ASTNode tryParse(Lexer lexer) {
    if(match(lexer)){
      return parse(lexer);
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "{");
  }

  @Override
  public ASTNode parse(Lexer lexer) {
    lexer.read();
    Token next = lexer.peek();
    if (isSkip(next, "}")) {
      lexer.read();
      return new ASTNode() {
        @Override
        public Object eval(EvalContext context, Type targetType, Annotation[] targetAnnotations) {
          if (targetType.equals(Object.class) || targetType.equals(Map.class)) {
            return Collections.emptyMap();
          } else {
            if (targetType instanceof Class) {
              return ReflectorFactory.DEFAULT.reflector((Class<?>) targetType).newInstance();
            }
            throw new ExprEvalException("Unsupport Type :" + targetType);
          }
        }
      };
    } else {
      ASTNode keyNode = stringParser.tryParse(lexer);
      Token token = lexer.read();
      if (!isSkip(token, ":")) {
        throw new UnExpectTokenException(lexer.expr(), token, ":");
      }
      ASTNode valueNode = valueParser.tryParse(lexer);
      List<Member> memberList = new LinkedList<>();
      Member member = new Member(keyNode, valueNode);
      memberList.add(member);
      while (!isSkip(lexer.peek(), "}")) {
        Member moreMember = parseMember(lexer);
        memberList.add(moreMember);
      }
      lexer.read();
      return new ASTNode() {
        @Override
        public Object eval(EvalContext context, Type targetType, Annotation[] targetAnnotations) {
          if (targetType.equals(Object.class) || targetType.equals(Map.class)) {
            Map<String, Object> objectMap = new HashMap<>();
            for (Member member : memberList) {
              objectMap.put(String.valueOf(member.keyNode.eval(context, String.class, null)),
                  member.valueNode.eval(context, Object.class, null));
            }
            return objectMap;
          } else {
            Reflector reflector = ReflectorFactory.DEFAULT.reflector((Class<?>) targetType);
            Object target = reflector.newInstance();
            for (Member member : memberList) {
              String fieldName = (String) member.keyNode.eval(context, String.class, null);
              PropertyMeta propertyMeta = reflector.propertyMeta(fieldName);
              Object value = member.valueNode.eval(context, propertyMeta.type(), propertyMeta
                  .annotations());
              reflector.setter(fieldName).set(target, value);
            }
            return target;
          }
        }
      };
    }
  }

  private Member parseMember(Lexer lexer) {
    Token token = lexer.read();
    if (!isSkip(token, ",")) {
      throw new UnExpectTokenException(lexer.expr(), token, ",");
    }
    ASTNode keyNode = stringParser.tryParse(lexer);
    Token next = lexer.read();
    if (!isSkip(next, ":")) {
      throw new UnExpectTokenException(lexer.expr(), token, ":");
    }
    ASTNode valueNode = valueParser.tryParse(lexer);
    return new Member(keyNode, valueNode);
  }

  private static class Member {

    private ASTNode keyNode;
    private ASTNode valueNode;

    public Member(ASTNode keyNode, ASTNode valueNode) {
      this.keyNode = keyNode;
      this.valueNode = valueNode;
    }
  }

  @Override
  public boolean match(Lexer lexer) {
    return isSkip(lexer.peek(), "{");
  }

  private static boolean isSkip(Token token, String text) {
    return token instanceof SkipToken && token.text().equals(text);
  }

}
