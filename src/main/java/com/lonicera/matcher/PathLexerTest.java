package com.lonicera.matcher;

import com.lonicera.token.Token;

public class PathLexerTest {

	public static void main(String[] args) {
		String path = "video/BV1Fy/4y1B/7Cc?from=search&seid=15416050337474933419";
		PathStringLexer lexter = new PathStringLexer(path);
		Token token;
/*		while((token = lexter.nextToken()) != Token.EOF) {
			System.out.println(token.toString());
		}*/
		int i = 1;
		while((token = lexter.lookAhead(i)) != Token.EOL){
			System.out.println(token.toString());
			i++;
		}
	}

}
