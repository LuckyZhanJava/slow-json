package com.lonicera.matcher;

import java.util.LinkedList;
import java.util.List;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.Node;
import com.lonicera.node.NormalTokenNode;
import com.lonicera.node.SeriesTokenNode;
import com.lonicera.parse.Parse;
import com.lonicera.parse.UnexpectedTokenException;
import com.lonicera.token.Token;
import com.lonicera.token.TypeToken;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class SeriesTokenParse extends Parse {
	private TypeToken typeToken;
	private String name;
	private Token[] tokens;

	private SeriesTokenParse(TypeToken typeToken, String name, Token... tokens){
		this.typeToken = typeToken;
		this.name = name;
		this.tokens = tokens;
	}

	@SafeVarargs
	public static SeriesTokenParse of(TypeToken typeToken, String name, Token... tokens){
		return new SeriesTokenParse(typeToken, name, tokens);
	}

	@Override
	protected boolean match(Lexer lexer) {
		for(int i = 0; i < tokens.length; i++) {
			Token expectToken = tokens[i];
			if(!match(lexer, expectToken, i)) {
				return false;				
			}
		}
		return true;
	}



	private boolean match(Lexer lexer, Token expectToken, int i) {
		Token nextToken = lexer.lookAhead(i + 1);
		return nextToken.getClass().equals(expectToken.getClass()) && nextToken.text().equals(expectToken.text());
	}

	@Override
	public Node<Token> parse(Lexer lexer) {
		if(match(lexer)){
			List<Node<Token>> children = new LinkedList<>();
			for(int i = 0; i < tokens.length; i++) {
				Token token = lexer.nextToken();
				children.add(NormalTokenNode.newTokenNode(token));
			}
			return SeriesTokenNode.newTokenNode(typeToken, name, children);
		}
		throw new UnexpectedTokenException(tokens[tokens.length - 1].text(), lexer.lookAhead(1).text());
	}

}
