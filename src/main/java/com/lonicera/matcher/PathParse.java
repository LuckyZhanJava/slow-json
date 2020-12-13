package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.Node;
import com.lonicera.parse.Parse;
import com.lonicera.token.Token;

public class PathParse{
	private Parse pathParse;
	public PathParse(){
	}

	public Node<Token> parse(Lexer lexer) {
		return pathParse.parse(lexer);
	}

	public static void main(String[] args) {
	/*	String path = "/video/BV1Fy/4y1B/7Cc?from=search&seid=15416050337474933419";;
		TokenParse separateParse = TokenParse.of(new PathSeparateToken());
		TokenParse eofParse = TokenParse.of(Token.EOF);
		PathStringLexer lexter = new PathStringLexer(path);
		BNFParseBuilder
		.newBuilder()
		.repeat(TokenParse.of(new PathSeparateToken(), new PathSegmentToken("video"))) 
		.sep(NextTokenParse.newParse(new PathSegmentToken("7Cc")))
		.sep(TokenParse.of(new PathSegmentToken("7Cc")))
		.option(separateParse)
		.sep(eofParse)
		.build()
		.parse(lexter);*/
	}
}
