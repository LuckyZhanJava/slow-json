package com.lonicera.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Reflector {

  private Class<?> clazz;
  private Map<String, Setter> PROPERTY_SETTER_MAP;
  private Map<String, PropertyMeta> PROPERTY_META_MAP;
  private Constructor<?> DEFAULT_CONSTRUCTOR;

  public Reflector(Class<?> clazz) {
    this.clazz = clazz;
    Map<String, Method> setterMethodMap = mapClassSetterMethods(clazz.getDeclaredMethods());
    Map<String, Field> nameFieldMap = mapClassFields(clazz.getDeclaredFields());
    PROPERTY_SETTER_MAP = propertySetterMap(setterMethodMap, nameFieldMap);
    PROPERTY_META_MAP = propertyMetaMap(setterMethodMap, nameFieldMap);
    DEFAULT_CONSTRUCTOR = defaultConstructor(clazz);
  }

  private Constructor<?> defaultConstructor(Class<?> clazz) {
    try {
      Constructor<?> constructor = clazz.getConstructor();
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor;
    } catch (NoSuchMethodException e) {
      throw new ReflectionException("class : " + clazz + " require default constructor");
    }
  }

  private Map<String, PropertyMeta> propertyMetaMap(Map<String, Method> setterMethodMap,
      Map<String, Field> nameFieldMap) {
    Map<String, PropertyMeta> propertyMetaMap = new HashMap<>();

    for (Entry<String, Field> entry : nameFieldMap.entrySet()) {
      propertyMetaMap.put(entry.getKey(), fieldMeta(entry.getValue()));
    }

    for (Entry<String, Method> entry : setterMethodMap.entrySet()) {
      propertyMetaMap.put(entry.getKey(), methodMeta(entry.getValue()));
    }

    return propertyMetaMap;
  }

  private PropertyMeta fieldMeta(Field value) {
    return new PropertyMeta() {
      @Override
      public Type type() {
        return value.getGenericType();
      }

      @Override
      public Annotation[] annotations() {
        return value.getDeclaredAnnotations();
      }
    };
  }

  private Map<String, Setter> propertySetterMap(Map<String, Method> setterMethodMap,
      Map<String, Field> nameFieldMap) {
    Map<String, Setter> setterMap = new HashMap<>();

    for (Entry<String, Field> entry : nameFieldMap.entrySet()) {
      setterMap.put(entry.getKey(), fieldSetter(entry.getValue()));
    }

    for (Entry<String, Method> entry : setterMethodMap.entrySet()) {
      setterMap.put(entry.getKey(), methodSetter(entry.getValue()));
    }

    return setterMap;
  }

  private Setter methodSetter(Method method) {
    if (!method.isAccessible()) {
      method.setAccessible(true);
    }
    return new Setter() {
      @Override
      public void set(Object o, Object value) {
        try {
          method.invoke(o, value);
        } catch (IllegalAccessException e) {
          throw new ReflectionException(e);
        } catch (InvocationTargetException e) {
          throw new ReflectionException(e);
        }
      }
    };
  }

  private PropertyMeta methodMeta(Method method) {
    return new PropertyMeta() {
      @Override
      public Type type() {
        return method.getGenericParameterTypes()[0];
      }

      @Override
      public Annotation[] annotations() {
        return method.getAnnotations();
      }
    };
  }

  private Setter fieldSetter(Field field) {
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }
    return new Setter() {
      @Override
      public void set(Object o, Object value) {
        try {
          field.set(o, value);
        } catch (IllegalAccessException e) {
          throw new ReflectionException(e);
        }
      }
    };
  }

  private Map<String, Method> mapClassSetterMethods(Method[] methods) {
    Map<String, Method> methodMap = new HashMap<>();
    for (Method method : methods) {
      String methodName = method.getName();
      if (methodName.length() > 3 && methodName.startsWith("set")
          && method.getParameterCount() == 1) {
        String propertyName = pureSetterProperty(methodName);
        Method exists = methodMap.put(propertyName, method);
        if (exists != null) {
          throw new ReflectionException("Not Support Overload Method :" + method + " & " + exists);
        }
      }
    }
    return methodMap;
  }

  private String pureSetterProperty(String methodName) {
    char[] chars = methodName.toCharArray();
    char c = chars[3];
    if (c >= 'A' && c <= 'Z') {
      c += 32;
      chars[3] = c;
    }
    return new String(chars, 3, chars.length - 3);
  }

  private Map<String, Field> mapClassFields(Field[] fields) {
    Map<String, Field> fieldMap = new HashMap<>();
    for (Field field : fields) {
      fieldMap.put(field.getName(), field);
    }
    return fieldMap;
  }

  public Object newInstance() {
    try {
      return DEFAULT_CONSTRUCTOR.newInstance();
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }

  public interface Setter {

    void set(Object o, Object value);
  }

  public Setter setter(String propertyName) {
    return PROPERTY_SETTER_MAP.get(propertyName);
  }

  public interface PropertyMeta {

    Type type();

    Annotation[] annotations();
  }

  public PropertyMeta propertyMeta(String propertyName) {
    return PROPERTY_META_MAP.get(propertyName);
  }
}
