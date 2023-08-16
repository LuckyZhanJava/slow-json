package com.lonicera.serializer;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeSerializer implements TypeSerializer {

  @Override
  public Object deserialize(Type type, Annotation[] fieldAnnotations, Object value) {
    if(value.getClass().isArray()){
      Object[] array = (Object[]) value;
      if(type.equals(LocalDateTime.class)){
        return LocalDateTime.of(
            intVal(array[0]),
            intVal(array[1]),
            intVal(array[2]),
            intVal(array[3]),
            intVal(array[4]),
            intVal(array[5]),
            intVal(array[6])
        );
      }
    } else {

    }
    return null;
  }

  @Override
  public boolean supportTarget(Class<?> clazz) {
    return clazz.equals(LocalDateTime.class) || clazz.equals(LocalDate.class);
  }

  private int intVal(Object o) {
    return Integer.valueOf(o.toString());
  }
}
