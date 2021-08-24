package com.lonicera.parser;

import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;

public class ValueParser implements Parser {
    private Parser[] parsers;
    public ValueParser(){
    }

    public Parser or(Parser... parsers){
        this.parsers = parsers;
        return this;
    }


    @Override
    public ASTNode parse(Lexer lexer){
        for(Parser parser : parsers){
            if(parser.match(lexer)){
                return parser.parse(lexer);
            }
        }
        throw new Error("should not happen");
    }

    @Override
    public boolean match(Lexer lexer){
        for(Parser parser : parsers){
            if(parser.match(lexer)){
                return true;
            }
        }
        return false;
    }

}
