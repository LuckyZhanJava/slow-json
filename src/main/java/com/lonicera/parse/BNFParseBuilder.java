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

  private Parse repeatParse(final Parse parse) {
    return new Parse() {
      private List<Token> tokenList = new LinkedList<>();
      @Override
      public boolean match(Lexer lexer) {
        while(parse.match(lexer)){
          tokenList.addAll(Arrays.asList(parse.parse(lexer)));
        }
        return true;
      }

      @Override
      public Token[] parse(Lexer lexer) {
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
      private Token[] tokens;
      @Override
      public boolean match(Lexer lexer) {
        tokens = parse.match(lexer) ? new Token[0] : parse.parse(lexer);
        return true;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        return tokens;
      }
    };
  }

  public BNFParseBuilder or(Parse... parses){
    parseList.add(orParse(parses));
    return this;
  }

  private Parse orParse(final Parse... parses) {
    return new Parse(){
      private Token[] tokens;
      @Override
      public boolean match(Lexer lexer) {
        boolean match = false;
        for(Parse parse : parses){
          if(parse.match(lexer)){
            tokens = parse.parse(lexer);
            return true;
          }
        }
        return match;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        return tokens;
      }
    };
  }

  public Parse build(){
    return new Parse() {
      private List<Token> tokenList = new LinkedList<>();
      @Override
      public boolean match(Lexer lexer) {
        for(Parse parse : parseList){
          if(parse.match(lexer)){
            return false;
          }else{
            Token[] tokens = parse.parse(lexer);
            tokenList.addAll(Arrays.asList(tokens));
          }
        }
        return true;
      }

      @Override
      public Token[] parse(Lexer lexer) {
        if(match(lexer)){
          return tokenList.toArray(new Token[tokenList.size()]);
        }
        throw new IllegalStateException("error match state");
      }
    };
  }
}
