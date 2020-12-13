package com.lonicera.matcher;

public class LexerReadException extends RuntimeException {

	private static final long serialVersionUID = -4667993485585858975L;
	
	public LexerReadException(Exception e) {
		super(e);
	}
	
	public LexerReadException(String message) {
		super(message);
	}
}
