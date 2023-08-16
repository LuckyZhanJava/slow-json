package com.lonicera.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ClassCacheReflectorFactory implements ReflectorFactory {
  private Map<Class<?>, Reflector> CLASS_REFLECTOR_MAP = new ConcurrentHashMap<>();
  @Override
  public Reflector reflector(Class<?> clazz) {
    if(CLASS_REFLECTOR_MAP.containsKey(clazz)){
      return CLASS_REFLECTOR_MAP.get(clazz);
    }
    Reflector reflector = new Reflector(clazz);
    CLASS_REFLECTOR_MAP.put(clazz, reflector);
    return reflector;
  }
}
