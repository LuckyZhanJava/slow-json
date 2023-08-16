package com.lonicera.serializer;


import com.lonicera.exception.ExprEvalException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializer implements TypeSerializer {

  @Override
  public Object deserialize(Type type, Annotation[] fieldAnnotations, Object value) {
    Class<?> clazz = (Class<?>) type;
    if(!(value instanceof String)){
      throw new ExprEvalException("Not Expect Token");
    }
    String stringValue = (String) value;

    if(clazz == BigDecimal.class){
      char[] chars = stringValue.toCharArray();
      return new BigDecimal(chars, 0, chars.length);
    }
    if(clazz == BigInteger.class){
      return new BigInteger(stringValue);
    }
    if(clazz == Long.class || clazz == long.class){
      return Long.parseLong(stringValue);
    }
    if(clazz == Double.class || clazz == double.class){
      return Double.parseDouble(stringValue);
    }
    if(clazz == Float.class || clazz == float.class){
      return Double.parseDouble(stringValue);
    }
    if(clazz == Integer.class || clazz == int.class){
      return Integer.parseInt(stringValue);
    }
    if(clazz == Short.class || clazz == short.class){
      return Short.parseShort(stringValue);
    }
    if(clazz == Byte.class || clazz == byte.class){
      return Byte.parseByte(stringValue);
    }
    return null;
  }

  @Override
  public boolean supportTarget(Class<?> clazz) {
    return Number.class.isAssignableFrom(clazz)
        || clazz.equals(long.class)
        || clazz.equals(double.class)
        || clazz.equals(int.class)
        || clazz.equals(float.class)
        || clazz.equals(short.class)
        || clazz.equals(byte.class)
        ;
  }
}
