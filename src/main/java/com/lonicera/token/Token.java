package com.lonicera.token;


public interface Token {
	Token EOF = new Token(){
		public String value() {
			return "EOF_TOKEN";
		}};
	
	String value();
}
