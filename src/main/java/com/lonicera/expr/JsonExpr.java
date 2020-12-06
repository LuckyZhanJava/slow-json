package com.lonicera.expr;

import com.lonicera.node.Node;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-11月-25日
 **/
public class JsonExpr implements Expr {

	public boolean match() {
		or();
		return false;
	}

	public Node<Token> make() {
		return null;
	}

	public boolean skip(Token t) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean option(Token t) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean repeat(Token t) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean or(Token... ts) {
		// TODO Auto-generated method stub
		return false;
	}
}
