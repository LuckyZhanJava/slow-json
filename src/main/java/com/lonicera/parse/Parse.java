package com.lonicera.parse;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.Node;
import com.lonicera.token.Token;

public abstract class Parse {
  protected abstract boolean match(Lexer lexer);
  public abstract Node<Token> parse(Lexer lexer);
}
