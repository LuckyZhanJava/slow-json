package com.lonicera.matcher;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import com.lonicera.token.Token;

public final class PathStringLexter implements Lexter {
	private StringReader stringReader;
	private Queue<Token> queue;
	private boolean readEnd;
	private static final int SEPARATOR = '/';
	private static final int PATH_END = '?';
	private static final int EOF = -1;
	private static final int EMPTY_CHAR = -2;
	private int nextChar = EMPTY_CHAR;

	public PathStringLexter(String path) {
		Objects.requireNonNull(path, "path must not be null");
		stringReader = new StringReader(path);
		readEnd = false;
		queue = new LinkedList<Token>();
	}

	@Override
	public Token nextToken() {
		if(!queue.isEmpty()) {
			return queue.remove();
		}

		int next = nextChar();
		if(next == PATH_END || next == EOF) {
			return Token.EOF;
		}

		if(next == SEPARATOR){
			return new SkipToken((char)next);
		}

		StringBuilder sb = new StringBuilder();
		while(next != SEPARATOR && next != PATH_END && next != EOF) {
			sb.append((char)next);
			next = nextChar();
		}
		ungetChar(next);
		Token pathToken = new PathToken(sb.toString());
		return pathToken;
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


	@Override
	public Token lookAhead() {
		Token token = nextToken();
		queue.add(token);
		return token;
	}

}
