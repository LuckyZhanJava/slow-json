package com.lonicera.matcher;

import java.util.LinkedList;
import java.util.List;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.NextTokenNode;
import com.lonicera.node.Node;
import com.lonicera.node.NormalTokenNode;
import com.lonicera.parse.Parse;
import com.lonicera.parse.UnexpectedTokenException;
import com.lonicera.token.Token;
import com.lonicera.token.TypeToken;

public class URIPathParse extends Parse{
	private TypeToken tokenType;
	private Token nextToken;
	private String name;
	private URIPathParse(TypeToken tokenType, String name, Token nextToken) {
		this.tokenType = tokenType;
		this.nextToken = nextToken;
		this.name = name;
	}
	
	@Override
	protected boolean match(Lexer lexer) {
		int i = 0;
		Token next = lexer.lookAhead(i + 1);
		while(!next.equals(nextToken)) {
			if(next == Token.EOL) {
				return false;
			}
			i++;
			next = lexer.lookAhead(i + 1);
		}
		return i != 0 && next.equals(nextToken);
	}

	@Override
	public Node<Token> parse(Lexer lexer) {
		if(match(lexer)) {
			return parse(lexer, nextToken);
		}
		throw new UnexpectedTokenException("Next is [" + nextToken.text() + "]", lexer.lookAhead(1).text());
	}

	private Node<Token> parse(Lexer lexer, Token nextToken) {
		List<Node<Token>> children = new LinkedList<>();
		Token next = lexer.lookAhead(1);
		while(!next.equals(nextToken)) {
			children.add(NormalTokenNode.newTokenNode(lexer.nextToken()));
			next = lexer.lookAhead(1);
		}
		return NextTokenNode.newNextTokenNode(tokenType, name, children);
	}
	
	public static URIPathParse newParse(TypeToken tokenType, String name, Token nextToken) {
		URIPathParse parse = new URIPathParse(tokenType, name, nextToken);
		return parse;
	}
}
