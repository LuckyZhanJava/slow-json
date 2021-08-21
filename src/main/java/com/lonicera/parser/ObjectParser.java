package com.lonicera.parser;

import com.lonicera.environment.Environment;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.SkipToken;
import com.lonicera.token.Token;
import java.lang.reflect.Method;
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
  public ASTNode parse(Lexer lexer) {
    if (match(lexer)) {
      lexer.read();
      Token next = lexer.peek();
      if (isSkip(next, "}")) {
        lexer.read();
        return new ASTNode() {
          @Override
          public Object eval(Environment env) {
            return new HashMap<>();
          }
        };
      } else {
        ASTNode keyNode = stringParser.parse(lexer);
        Token token = lexer.read();
        if (!isSkip(token, ":")) {
          throw new UnExpectTokenException(lexer.expr(), token, ":");
        }
        ASTNode valueNode = valueParser.parse(lexer);
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
          public Object eval(Environment env) {
            Map<String, Object> objectMap = new HashMap<>();
            for (Member member : memberList) {
              objectMap.put(String.valueOf(member.keyNode.eval(env)), member.valueNode.eval(env));
            }
            return objectMap;
          }
        };
      }
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "{");
  }

  private Member parseMember(Lexer lexer) {
    Token token = lexer.read();
    if (!isSkip(token, ",")) {
      throw new UnExpectTokenException(lexer.expr(), token, ",");
    }
    ASTNode keyNode = stringParser.parse(lexer);
    Token next = lexer.read();
    if (!isSkip(next, ":")) {
      throw new UnExpectTokenException(lexer.expr(), token, ":");
    }
    ASTNode valueNode = valueParser.parse(lexer);
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
