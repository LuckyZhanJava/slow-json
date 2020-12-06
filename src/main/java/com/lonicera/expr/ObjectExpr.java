package com.lonicera.expr;

import com.lonicera.node.Node;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-11月-25日
 **/
public class ObjectExpr implements Expr {

	public boolean match() {
		or();
		return false;
	}

	public Node<Token> make() {
		return null;
	}

	public void option(Expr expr) {

	}

	public void repeat(Expr expr) {

	}

	public void or(Expr... exprs) {

	}
}
