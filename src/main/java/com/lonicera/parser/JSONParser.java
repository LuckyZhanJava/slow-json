package com.lonicera.parser;

import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.Token;

public class JSONParser implements Parser {

  private Parser[] parsers;

  public JSONParser() {

  }

  public JSONParser or(Parser... parsers) {
    this.parsers = parsers;
    return this;
  }

  @Override
  public ASTNode parse(Lexer lexer) {
    if (match(lexer)) {
      ASTNode astNode = doParse(lexer);
      Token token = lexer.peek();
      if (token != Token.EOF) {
        throw new UnExpectTokenException(lexer.expr(), token, "[EOF]");
      }
      return astNode;
    }
    throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "{");
  }

  private ASTNode doParse(Lexer lexer) {
    for (Parser parser : parsers) {
      if (parser.match(lexer)) {
        return parser.parse(lexer);
      }
    }
    throw new Error();
  }

  @Override
  public boolean match(Lexer lexer) {
    for (Parser parser : parsers) {
      if (parser.match(lexer)) {
        return true;
      }
    }
    return false;
  }

}
