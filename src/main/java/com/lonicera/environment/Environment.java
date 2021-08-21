package com.lonicera.environment;


public interface Environment {
  Object putIfAbsent(String key, Object o);
  Object put(String key, Object o);
  Object remove(String key);
  Object get(String key);
}
