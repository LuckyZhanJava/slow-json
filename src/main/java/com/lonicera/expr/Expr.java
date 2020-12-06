package com.lonicera.expr;

import com.lonicera.node.Node;
import com.lonicera.token.Token;

public interface Expr {
  boolean match();
  Node<Token> make();
}
