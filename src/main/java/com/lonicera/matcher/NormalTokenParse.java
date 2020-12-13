package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.Node;
import com.lonicera.node.NormalTokenNode;
import com.lonicera.parse.Parse;
import com.lonicera.parse.UnexpectedTokenException;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class NormalTokenParse extends Parse {
	private Token token;

	private NormalTokenParse(Token token){
		this.token = token;
	}

	public static NormalTokenParse of(Token token){
		return new NormalTokenParse(token);
	}

	@Override
	protected boolean match(Lexer lexer) {
		Token next = lexer.lookAhead(1);
		return next.getClass().equals(token.getClass()) && next.text().equals(token.text());
	}

	@Override
	public Node<Token> parse(Lexer lexer) {
		if(match(lexer)){
			return NormalTokenNode.newTokenNode(lexer.nextToken());
		}
		throw new UnexpectedTokenException(token.text(), lexer.lookAhead(1).text());
	}

}
