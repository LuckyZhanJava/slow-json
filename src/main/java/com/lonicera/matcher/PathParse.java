package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.parse.BNFParseBuilder;
import com.lonicera.parse.CombineParse;
import com.lonicera.parse.Parse;
import com.lonicera.token.Token;

public class PathParse{
  private Parse pathParse;
  public PathParse(){
    SkipParse skipParse = SkipParse.of(new SkipToken("/"));
    pathParse = BNFParseBuilder
        .newInstance()
        .repeat(CombineParse.of(skipParse, new PathSegmentParse()))
        .option(skipParse)
        .build();
  }

  public Token[] parse(Lexer lexer) {
    return pathParse.parse(lexer);
  }

  public static void main(String[] args) {
    String path = "video/BV1Fy/4y1B/7Cc?from=search&seid=15416050337474933419";
    PathStringLexer lexter = new PathStringLexer(path);
    new PathParse().parse(lexter);
  }
}
