package com.lonicera.parse;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.lonicera.lexer.Lexer;
import com.lonicera.node.Node;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-11月-25日
 **/
public final class BNFParseBuilder{
	
	public static interface Factory{
		Node<Token> make(List<Node<Token>> nodeList);
	}
	
	private List<Parse> parseList;
	private Factory factory;

	private BNFParseBuilder(){
		parseList = new LinkedList<>();
	}

	public static BNFParseBuilder newBuilder(){
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
			public Node<Token> parse(Lexer lexer) {
				return parse.parse(lexer);
			}
		};
	}

	private Parse repeatParse(final Parse parse) {
		return new Parse() {

			@Override
			protected boolean match(Lexer lexer) {
				return true;
			}

			@Override
			public Node<Token> parse(Lexer lexer) {
				Node<Token> parsedNode = null;
				for(int i = parseList.size() - 1; i > -1; i--){
					parsedNode = parseList.get(i).parse(lexer);
				}
				return parsedNode;
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
				return parse.match(lexer);
			}

			@Override
			public Node<Token> parse(Lexer lexer) {
				return parse.match(lexer) ? parse.parse(lexer) : null;
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
			public Node<Token> parse(Lexer lexer) {
				Parse parse = choose(lexer);
				if(parse == null){
					throw new UnexpectedTokenException(null, lexer.lookAhead(1).text());
				}
				return parse.parse(lexer);
			}
		};
	}
	
	public BNFParseBuilder factory(Factory factory){
		this.factory = factory;
		return this;
	}

	public Parse build(){
		Objects.requireNonNull(factory);
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
			public Node<Token> parse(Lexer lexer) {
				List<Node<Token>> nodeList = new LinkedList<>();
				for(Parse parse : parseList){
					nodeList.add(parse.parse(lexer));						
				}
				return factory.make(nodeList);
			}
		};
	}
}
