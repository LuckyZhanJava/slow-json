package com.lonicera.token;


public interface Token {
	Token EOL = new EOLToken();
	
	String text();

	String toString();
}
