package com.lonicera.node;

import java.util.List;

public interface Node<T> {
  T getItem();
  List<Node<T>> getChildren();
}
