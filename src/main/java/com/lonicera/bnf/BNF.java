package com.lonicera.bnf;

public interface BNF<T> {
  void skip(T t);
  void option(T t);
  void repeat(T t);
  void or(T... ts);
}
