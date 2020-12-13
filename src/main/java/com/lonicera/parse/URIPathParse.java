package com.lonicera.parse;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.Node;
import com.lonicera.token.Token;

public class URIPathParse extends Parse {
	
	private Parse parse;
	
	private URIPathParse(Parse parse) {
		this.parse = parse;
	}

	@Override
	protected boolean match(Lexer lexer) {
		return parse.match(lexer);
	}

	@Override
	public Node<Token> parse(Lexer lexer) {
		Node<Token> leftNode = parse.parse(lexer);
		return null;
	}
	
	public static URIPathParse newParse(Parse parse) {
		return new URIPathParse(parse);
	}
}
