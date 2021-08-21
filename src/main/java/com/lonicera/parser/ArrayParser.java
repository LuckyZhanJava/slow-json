package com.lonicera.parser;

import com.lonicera.environment.Environment;
import com.lonicera.exception.UnExpectTokenException;
import com.lonicera.token.Lexer;
import com.lonicera.token.SkipToken;
import com.lonicera.token.Token;

import java.util.LinkedList;
import java.util.List;

public class ArrayParser implements Parser {
    private Parser elementParser;
    public ArrayParser(){
    }

    public Parser elementParser(Parser elementParser){
        this.elementParser = elementParser;
        return this;
    }


    @Override
    public ASTNode parse(Lexer lexer){
        if(match(lexer)){
            lexer.read();
            final List<ASTNode> children = new LinkedList<>();
            if(!isSkipToken(lexer.peek(), "]")){
                children.add(elementParser.parse(lexer));
            }
            while(!isSkipToken(lexer.peek(), "]")){
                Token next = lexer.read();
                if(!isSkipToken(next, ",")){
                    throw new UnExpectTokenException(lexer.expr(), next, ",");
                }
                ASTNode astNode = elementParser.parse(lexer);
                children.add(astNode);
            }
            lexer.read();
            return new ASTNode() {
                @Override
                public Object eval(Environment env) {
                    Object[] objects = new Object[children.size()];
                    for(int i = 0; i < children.size(); i++){
                        objects[i] = children.get(i).eval(env);
                    }
                    return objects;
                }
            };
        }
        throw new UnExpectTokenException(lexer.expr(), lexer.peek(), "[");
    }

    @Override
    public boolean match(Lexer lexer){
        return isSkipToken(lexer.peek(), "[");
    }

    private boolean isSkipToken(Token next, String expectText){
        return next instanceof SkipToken && next.text().equals(expectText);
    }
}
