package com.lonicera.parse;

import com.lonicera.lexer.Lexer;
import com.lonicera.token.Token;

public interface Parse {
  boolean match(Lexer lexer);
  Token[] parse(Lexer lexer);
}
