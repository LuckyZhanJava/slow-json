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
	private static final int separator = '/';
	private static final int pathEnd = '?';

	public PathStringLexter(String path) {
		Objects.requireNonNull(path, "path must not be null");
		stringReader = new StringReader(path);
		queue = new LinkedList<Token>();
	}

	public Token nextToken() {
		if(!queue.isEmpty()) {
			return queue.remove();
		}
		if(readEnd) {
			return Token.EOF;
		}
		StringBuilder sb = new StringBuilder();
		int next = nextUnskipChar(separator);
		while(next != -1 && next != separator && next != pathEnd) {
			sb.append((char)next);
			next = nextChar();
		}
		if(sb.length() == 0) {
			return Token.EOF;
		}
		if(next == pathEnd) {
			readEnd = true;
		}
		Token pathToken = new PathToken(sb.toString());
		return pathToken;
	}

	private int nextUnskipChar(int skipChar) {
		int nextChar = nextChar();
		while(nextChar == skipChar) {
			nextChar = nextChar();
		}
		return nextChar;
	}

	private int nextChar() {
		try {
			int start = stringReader.read();
			return start;
		} catch (IOException e) {
			throw new LexerReadException(e);
		}
	}


	public Token lookAhead() {
		Token token = nextToken();
		queue.add(token);
		return token;
	}

}
