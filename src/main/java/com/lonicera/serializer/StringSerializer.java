package com.lonicera.serializer;


import com.lonicera.exception.ExprEvalException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class StringSerializer implements TypeSerializer {

  @Override
  public Object deserialize(Type type, Annotation[] fieldAnnotations, Object value) {

    Class<?> clazz = (Class<?>) type;

    if(!(value instanceof String)){
      throw new ExprEvalException("Not Expect Token");
    }
    String stringValue = (String) value;
    if(clazz == String.class){
      return stringValue;
    }
    if(clazz == Character.class || clazz == char.class){
      return stringValue.charAt(0);
    }
    return null;
  }

  @Override
  public boolean supportTarget(Class<?> clazz) {
    return clazz.equals(String.class)
        || clazz.equals(Character.class)
        || clazz.equals(char.class)
        ;
  }
}
