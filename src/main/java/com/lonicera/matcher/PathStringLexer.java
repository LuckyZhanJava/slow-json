package com.lonicera.matcher;

import com.lonicera.lexer.Lexer;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PathStringLexer implements Lexer {
	private StringReader stringReader;
	private static final int SEPARATOR = '/';
	private static final int PATH_END = '?';
	private static final int EOF = -1;
	private static final int EMPTY_CHAR = -2;
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
		if(readEnd){
			throw new IllegalStateException("already read end");
		}

		if(!queue.isEmpty()) {
			return queue.remove(0);
		}

		return readNextToken();
	}

	private com.lonicera.token.Token readNextToken() {
		int next = nextChar();
		if(next == PATH_END || next == EOF) {
			readEnd = true;
			return com.lonicera.token.Token.EOF;
		}

		if(next == SEPARATOR){
			return new PathSeparateToken();
		}

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
