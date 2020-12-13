package com.lonicera.node;

import java.util.List;

import com.lonicera.token.Token;
import com.lonicera.token.TypeToken;


public class DeclareTokenNode implements Node<Token> {
	private TypeToken item;
	@SuppressWarnings("unchecked")
	private List<Node<Token>> children;
	
	private DeclareTokenNode() {
	}
	
	@Override
	public Token getItem() {
		return item;
	}

	@Override
	public List<com.lonicera.node.Node<Token>> getChildren() {
		return children;
	}

	public static DeclareTokenNode newNextTokenNode(TypeToken item, String name, List<Node<Token>> children) {
		DeclareTokenNode tokenNode = new DeclareTokenNode();
		tokenNode.item = item;
		tokenNode.children = children;
		return tokenNode;
	}
	
	@Override
	public String toString() {
		return "TokenNode [item=" + item + ", children=" + children + "]";
	}
}
