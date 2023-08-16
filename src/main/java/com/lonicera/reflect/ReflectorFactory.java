package com.lonicera.reflect;

public interface ReflectorFactory {

  ReflectorFactory DEFAULT  = new ClassCacheReflectorFactory();

  Reflector reflector(Class<?> clazz);
}
