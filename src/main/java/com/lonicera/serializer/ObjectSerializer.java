package com.lonicera.serializer;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class ObjectSerializer implements TypeSerializer {

  @Override
  public Object deserialize(Type type, Annotation[] fieldAnnotations, Object value) {
    return value;
  }

  @Override
  public boolean supportTarget(Class<?> clazz) {
    return clazz.equals(Object.class);
  }
}
