package com.lonicera.lexer;

import com.lonicera.token.Token;

public interface Lexer {
  Token nextToken();
  Token lookAhead(int k);
}
