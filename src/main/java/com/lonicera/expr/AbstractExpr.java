package com.lonicera.expr;

import com.lonicera.node.Node;
import com.lonicera.token.Token;

public abstract class AbstractExpr implements Expr{

	protected boolean skip(Token t) {
		return false;
	}

	protected boolean option(Token t) {
		return false;
	}

	protected boolean repeat(Token t) {
		return false;
	}

	protected boolean or(Token... ts) {
		return false;
	}

	public Node<Token> make() {
		return null;
	}

}
