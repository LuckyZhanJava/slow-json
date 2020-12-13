package com.lonicera.matcher;

import com.lonicera.token.AbstractToken;

/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class PathSeparateToken extends AbstractToken {
	private String path;

	public PathSeparateToken() {
		this.path = "/";
	}

	@Override
	public String text() {
		return path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}
}
