package com.lonicera.matcher;

import com.lonicera.node.Node;
import com.lonicera.parse.BNFParseBuilder;
import com.lonicera.parse.Parse;
import com.lonicera.token.Token;

public class PathParseGeneratorApp{

	public PathParseGeneratorApp(){

	}

	public static void main(String[] args) {
		String pattern = "/video/BV1Fy/{path}";
		String path = "/video/BV1Fy/4y1B/7Cc?from=search&seid=15416050337474933419";
		PathStringLexer lexer = new PathStringLexer(pattern);
		Token token;
		BNFParseBuilder builder = BNFParseBuilder.newBuilder();
		while((token = lexer.nextToken()) != Token.EOL){
			if(token instanceof PathVariableToken) {
				builder.sep(NextTokenParse.newParse(null, ((PathVariableToken)token).getVariableName(), lexer.lookAhead(1)));
			}else {
				builder.sep(NormalTokenParse.of(token));
			}
		}
		builder.sep(NormalTokenParse.of(Token.EOL));
		Parse pathParse = builder.build();
		
		Parse uriPathParse = BNFParseBuilder.newBuilder().sep(pathParse).build();
		
		Node<Token> parsedNode = uriPathParse.parse(new PathStringLexer(path));
		System.out.println(parsedNode);
	}
}
