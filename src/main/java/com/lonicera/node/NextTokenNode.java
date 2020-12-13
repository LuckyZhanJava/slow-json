package com.lonicera.node;

import java.util.List;

import com.lonicera.token.Token;
import com.lonicera.token.TypeToken;


public class NextTokenNode implements Node<Token> {
	private String name;
	private TypeToken item;
	@SuppressWarnings("unchecked")
	private List<Node<Token>> children;
	
	private NextTokenNode() {
	}
	
	@Override
	public Token getItem() {
		return item;
	}

	@Override
	public List<com.lonicera.node.Node<Token>> getChildren() {
		return children;
	}

	public static NextTokenNode newNextTokenNode(TypeToken item, String name, List<Node<Token>> children) {
		NextTokenNode tokenNode = new NextTokenNode();
		tokenNode.item = item;
		tokenNode.name = name;
		tokenNode.children = children;
		return tokenNode;
	}
	
	@Override
	public String toString() {
		return "TokenNode [item=" + item + ", children=" + children + "]";
	}
}
