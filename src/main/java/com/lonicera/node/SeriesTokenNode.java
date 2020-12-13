package com.lonicera.node;

import java.util.List;

import com.lonicera.token.Token;
import com.lonicera.token.TypeToken;


public class SeriesTokenNode implements Node<Token> {
	private String name;
	private TypeToken item;
	@SuppressWarnings("unchecked")
	private List<Node<Token>> children;
	
	private SeriesTokenNode() {
	}
	
	@Override
	public Token getItem() {
		return item;
	}

	@Override
	public List<com.lonicera.node.Node<Token>> getChildren() {
		return children;
	}

	public static SeriesTokenNode newTokenNode(TypeToken item, String name, List<Node<Token>> children) {
		SeriesTokenNode tokenNode = new SeriesTokenNode();
		tokenNode.item = item;
		tokenNode.name = name;
		tokenNode.children = children;
		return tokenNode;
	}

	@Override
	public String toString() {
		return "SeriesTokenNode [name=" + name + ", item=" + item + ", children=" + children + "]";
	}
}
