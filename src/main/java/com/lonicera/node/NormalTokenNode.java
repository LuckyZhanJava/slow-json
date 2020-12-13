package com.lonicera.node;

import java.util.Collections;
import java.util.List;

import com.lonicera.token.Token;


public class NormalTokenNode implements Node<Token> {
	
	private Token item;
	@SuppressWarnings("unchecked")
	private List<Node<Token>> children = Collections.EMPTY_LIST;
	
	private NormalTokenNode() {
	}
	
	@Override
	public Token getItem() {
		return item;
	}

	@Override
	public List<com.lonicera.node.Node<Token>> getChildren() {
		return children;
	}

	public static NormalTokenNode newTokenNode(Token item) {
		NormalTokenNode tokenNode = new NormalTokenNode();
		tokenNode.item = item;
		return tokenNode;
	}

	@Override
	public String toString() {
		return "NormalTokenNode [item=" + item + "]";
	}
}
