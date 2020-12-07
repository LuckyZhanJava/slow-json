package com.lonicera.token;


public interface Token {
	Token EOF = new Token(){
		public String text() {
			return "EOF_TOKEN";
		}};
	
	String text();

	String toString();
}
