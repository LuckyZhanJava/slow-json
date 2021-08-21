package com.lonicera.parser;


import com.lonicera.environment.Environment;

public interface ASTNode {
    Object eval(Environment env);
}
