package com.lonicera.parse;

import com.lonicera.lexer.Lexer;
import com.lonicera.token.Token;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author LiBowei
 * @date 2020年-11月-25日
 **/
public final class BNFParseBuilder{
  private List<Parse> parseList;

  private BNFParseBuilder(){
    parseList = new LinkedList<>();
  }

  public static BNFParseBuilder newInstance(){
    return new BNFParseBuilder();
  }

  public BNFParseBuilder repeat(Parse parse){
    parseList.add(repeatParse(parse));
    return this;
  }

  public BNFParseBuilder sep(Parse parse){
    parseList.add(sepParse(parse));
    return this;
  }

  private Parse sepParse(final Parse parse) {
    return new Parse() {
      @Override
      protected boolean match(Lexer lexer) {
        return parse.match(lexer);
      }

      @Override
      public Token[] parse(Lexer lexer) {
        return parse.parse(lexer);
      }
    };
  }

  private Parse repeatParse(final Parse parse) {
    return new Parse() {

      @Override
      public boolean match(Lexer lexer) {
        return true;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        List<Token> tokenList = new LinkedList<>();
        while(parse.match(lexer)){
          tokenList.addAll(Arrays.asList(parse.parse(lexer)));
        }
        return tokenList.toArray(new Token[tokenList.size()]);
      }
    };
  }

  public BNFParseBuilder option(Parse parse){
    parseList.add(optionParse(parse));
    return this;
  }

  private Parse optionParse(final Parse parse) {
    return new Parse() {
      @Override
      public boolean match(Lexer lexer) {
        return true;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        return parse.match(lexer) ? new Token[0] : parse.parse(lexer);
      }
    };
  }

  public BNFParseBuilder or(Parse... parses){
    parseList.add(orParse(parses));
    return this;
  }

  private Parse orParse(final Parse... parses) {
    return new Parse(){
      @Override
      public boolean match(Lexer lexer) {
        return choose(lexer) != null;
      }

      private Parse choose(Lexer lexer){
        for(Parse parse : parses){
          if(parse.match(lexer)){
            return parse;
          }
        }
        return null;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        Parse parse = choose(lexer);
        if(parse == null){
          throw new UnexpectTokenTypeException(null, lexer.lookAhead(1).getClass());
        }
        return parse.parse(lexer);
      }
    };
  }

  public Parse build(){
    return new Parse() {
      @Override
      public boolean match(Lexer lexer) {
        for(Parse parse : parseList){
          if(!parse.match(lexer)){
            return false;
          }
        }
        return true;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        List<Token> tokenList = new LinkedList<>();
        for(Parse parse : parseList){
          tokenList.addAll(Arrays.asList(parse.parse(lexer)));
        }
        return tokenList.toArray(new Token[tokenList.size()]);
      }
    };
  }
}
