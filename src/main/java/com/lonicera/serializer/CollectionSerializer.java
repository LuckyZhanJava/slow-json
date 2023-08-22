package com.lonicera.serializer;


import com.lonicera.exception.ExprEvalException;
import com.lonicera.reflect.ReflectorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CollectionSerializer implements TypeSerializer {

  @Override
  public Object deserialize(Type type, Annotation[] fieldAnnotations, Object value) {
    Collection collection;
    if (type instanceof Class) {
      collection = (Collection)ReflectorFactory.DEFAULT.reflector((Class<?>) type).newInstance();
    } else if (type instanceof ParameterizedType) {
      Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
      if (rawType.equals(List.class)) {
        collection = new LinkedList();
      } else if (rawType.equals(Set.class)) {
        collection = new HashSet();
      } else if (rawType.equals(Queue.class)) {
        collection = new ArrayDeque();
      } else {
        throw new ExprEvalException("");
      }
    } else {
      throw new ExprEvalException("");
    }
    for (Object val : (Object[]) value) {
      collection.add(val);
    }
    return collection;
  }

  @Override
  public boolean supportTarget(Class<?> clazz) {
    return Collection.class.isAssignableFrom(clazz);
  }
}
