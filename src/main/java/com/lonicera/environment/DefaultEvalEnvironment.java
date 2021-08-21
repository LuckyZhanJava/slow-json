package com.lonicera.environment;

import java.util.*;

public class DefaultEvalEnvironment implements Environment {

  private Map<String,Object> envMap = new HashMap<>();

  @Override
  public Object putIfAbsent(String key, Object o) {
    return envMap.putIfAbsent(key, o);
  }

  @Override
  public Object put(String key, Object o) {
    return envMap.put(key, o);
  }

  @Override
  public Object remove(String key) {
    return envMap.remove(key);
  }

  @Override
  public Object get(String key){
    return envMap.get(key);
  }

  public void registerClass(Class<?> clazz){
    Objects.requireNonNull(clazz);
  }

  private void putEnvValue(String name, Object value){
    if(envMap.containsKey(name)){
      ((List<Object>)envMap.get(name)).add(value);
    }else {
      List<Object> valueList = new LinkedList<>();
      valueList.add(value);
      envMap.put(name, valueList);
    }
  }

}
