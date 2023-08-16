package com.lonicera.parser;

import com.lonicera.token.Lexer;

public interface Parser {

  ASTNode tryParse(Lexer lexer);

  ASTNode parse(Lexer lexer);

  boolean match(Lexer lexer);
}
