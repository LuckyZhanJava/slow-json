package com.lonicera.token;

public interface Token {

  Token EOF = new Token() {
    @Override
    public String text() {
      return "<EOF>";
    }

    @Override
    public int offset() {
      return -1;
    }

    @Override
    public int length() {
      return 0;
    }
  };

  String text();

  int offset();

  int length();
}
