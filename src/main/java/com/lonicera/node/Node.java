package com.lonicera.node;

import java.util.List;

public interface Node<T> {
  T getElement();
  List<Node<T>> getChildren();
}
