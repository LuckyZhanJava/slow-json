package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.parse.BNFParseBuilder;
import com.lonicera.parse.CombineParse;
import com.lonicera.parse.Parse;
import com.lonicera.token.Token;

public class PathParse{
  private Parse pathParse;
  public PathParse(){
    TokenParse tokenParse = TokenParse.of(new PathSeparateToken());
    pathParse = BNFParseBuilder
        .newInstance()
        .repeat(CombineParse.of(tokenParse, new PathSegmentParse()))
        .option(tokenParse)
        .build();
  }

  public com.lonicera.token.Token[] parse(Lexer lexer) {
    return pathParse.parse(lexer);
  }

  public static void main(String[] args) {
    String path = "video/BV1Fy/4y1B/7Cc?from=search&seid=15416050337474933419";;
    TokenParse separateParse = TokenParse.of(new PathSeparateToken());
    TokenParse eofParse = TokenParse.of(Token.EOF);
    PathStringLexer lexter = new PathStringLexer(path);
    BNFParseBuilder
        .newInstance()
        .repeat(CombineParse.of(separateParse, new PathSegmentParse()))
        .option(separateParse)
        .sep(eofParse)
        .build()
        .parse(lexter);
  }
}
