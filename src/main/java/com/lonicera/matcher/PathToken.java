package com.lonicera.matcher;

import com.lonicera.token.Token;

public class PathToken implements Token {
	private String path;
	
	public PathToken(String path) {
		this.path = path;
	}

	public String value() {
		return path;
	}
}
