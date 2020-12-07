package com.lonicera.matcher;

import com.lonicera.token.AbstractToken;

public class PathToken extends AbstractToken {
	private String path;
	
	public PathToken(String path) {
		this.path = path;
	}

	public String text() {
		return path;
	}
}
