package com.lonicera.parse;


/**
 * @author LiBowei
 * @date 2020年-12月-11日
 **/
public class UnexpectedTokenException extends RuntimeException{

	private static final long serialVersionUID = 1479367258337969915L;
	
	private String expect;
	private String present;
	
	public UnexpectedTokenException(String expect, String present){
		this.expect = expect;
		this.present = present;
	}

	@Override
	public String getMessage() {
		return "Expect: " + String.valueOf(expect) + ", Present: " + present.toString();
	}
}
