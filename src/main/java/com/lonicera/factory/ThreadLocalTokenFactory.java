package com.lonicera.factory;

import com.lonicera.token.BooleanToken;
import com.lonicera.token.NullToken;
import com.lonicera.token.NumberToken;
import com.lonicera.token.SkipToken;
import com.lonicera.token.StringToken;
import com.lonicera.token.Token;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ThreadLocalTokenFactory implements TokenFactory, Recycleable {

  private final ThreadLocal<Map<Class<? extends Token>, TokenCache>> LOCAL_TOKEN_CACHE_MAP = ThreadLocal
      .withInitial(() -> newTokenCacheMap());

  private Map<Class<? extends Token>, TokenCache> newTokenCacheMap() {
    Map<Class<? extends Token>, TokenCache> tokenCacheMap = new HashMap<Class<? extends Token>, TokenCache>() {
      {
        put(BooleanToken.class, newTokenCache(20, () -> new BooleanToken()));
        put(NullToken.class, newTokenCache(20, () -> new NullToken()));
        put(NumberToken.class, newTokenCache(20, () -> new NumberToken()));
        put(SkipToken.class, newTokenCache(50, () -> new SkipToken()));
        put(StringToken.class, newTokenCache(20, () -> new StringToken()));
      }
    };
    return tokenCacheMap;
  }

  private TokenCache newTokenCache(int count, Supplier<? extends Token> supplier) {
    TokenCache tokenCache = new TokenCache();
    tokenCache.tokens = initTokenCache(supplier, count);
    tokenCache.takeIndex = 0;
    tokenCache.provider = supplier;
    return tokenCache;
  }

  private Token[] initTokenCache(Supplier<? extends Token> supplier, int count) {
    Token[] tokens = new Token[count];
    for (int i = 0; i < count; i++) {
      tokens[i] = supplier.get();
    }
    return tokens;
  }

  private class TokenCache {

    private Supplier<? extends Token> provider;
    private Token[] tokens;
    private int takeIndex = 0;

    public Token take() {
      if (takeIndex < tokens.length) {
        Token take = tokens[takeIndex];
        takeIndex++;
        return take;
      }
      return provider.get();
    }

    public void recycle() {
      takeIndex = 0;
    }
  }

  @Override
  public <T extends Token> T token(Class<T> clazz) {
    Token token = LOCAL_TOKEN_CACHE_MAP.get().get(clazz).take();
    return (T) token;
  }

  @Override
  public void recycle() {
    LOCAL_TOKEN_CACHE_MAP.get().values().forEach(TokenCache::recycle);
  }
}
