package com.lonicera.factory;

import com.lonicera.token.Token;

public interface TokenFactory {
  <T extends Token> T token(Class<T> clazz);
}
