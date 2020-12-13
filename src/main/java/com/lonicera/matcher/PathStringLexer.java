package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import com.lonicera.token.Token;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class PathStringLexer implements Lexer {
	private static final Logger logger = Logger.getLogger(PathStringLexer.class.getName());
	private StringReader stringReader;
	private static final int SEPARATOR = '/';
	private static final int PATH_END = '?';
	private static final int EOF = -1;
	private static final int EMPTY_CHAR = -2;
	private static final int LEFT_BRACKET = '{';
	private static final int RIGHT_BRACKET = '}';
	private int nextChar = EMPTY_CHAR;
	private List<com.lonicera.token.Token> queue;
	private boolean readEnd;

	public PathStringLexer(String path) {
		Objects.requireNonNull(path, "path must not be null");
		stringReader = new StringReader(path);
		queue = new ArrayList<>();
		readEnd = false;
	}

	@Override
	public com.lonicera.token.Token nextToken() {
		Token token;
		if(!queue.isEmpty()) {
			token = queue.remove(0);
		}else {
			token = readNextToken();
		}
		logger.info("next token :" + token);
		return token;
	}

	private com.lonicera.token.Token readNextToken() {
		if(readEnd){
			throw new LexerReadException("alreay read end");
		}
		
		int next = nextChar();
		if(next == PATH_END || next == EOF) {
			readEnd = true;
			return com.lonicera.token.Token.EOL;
		}

		if(next == SEPARATOR){
			return new PathSeparateToken();
		}
		
		if(next == LEFT_BRACKET) {
			return readPathVariableToken();
		}

		return readPathSegmentToken(next);
	}

	private Token readPathVariableToken() {
		StringBuilder sb = new StringBuilder();
		int next = nextChar();
		while(next != RIGHT_BRACKET) {
			if(next == EOF) {
				throw new LexerReadException("expect '}' end");
			}
			sb.append((char)next);
			next = nextChar();
		}
		return new PathVariableToken(sb.toString());
	}

	private Token readPathSegmentToken(int next) {
		StringBuilder sb = new StringBuilder();
		while(next != SEPARATOR && next != PATH_END && next != EOF) {
			sb.append((char)next);
			next = nextChar();
		}
		ungetChar(next);
		com.lonicera.token.Token pathToken = new PathSegmentToken(sb.toString());
		return pathToken;
	}
	
	@Override
	public com.lonicera.token.Token lookAhead(int k) {
		if(k < 1){
			throw new IllegalStateException("k must > 0");
		}
		int readedCount = queue.size();
		if(k < readedCount){
			return queue.get(k - 1);
		}

		for(int i = 0; i < k - readedCount; i++){
			com.lonicera.token.Token nextToken = readNextToken();
			queue.add(nextToken);
		}
		return queue.get(queue.size() - 1);
	}

	private void ungetChar(int next) {
		this.nextChar = next;
	}

	private int nextChar() {
		try {
			if(nextChar == EMPTY_CHAR){
				int c = stringReader.read();
				return c;
			} else {
				int c = nextChar;
				nextChar = EMPTY_CHAR;
				return c;
			}
		} catch (IOException e) {
			throw new LexerReadException(e);
		}
	}

}
