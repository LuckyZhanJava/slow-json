package com.lonicera.context;


import com.lonicera.serializer.TypeSerializer;

public interface EvalContext {
  TypeSerializer getTypeSerializer(Class<?> clazz);
}
