package com.lonicera.serializer;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface TypeSerializer {
  Object deserialize(Type type, Annotation[] fieldAnnotations, Object value);
  boolean supportTarget(Class<?> clazz);
}
