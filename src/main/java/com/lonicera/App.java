package com.lonicera;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lonicera.json.SlowJson;
import com.lonicera.serializer.CollectionSerializer;
import com.lonicera.serializer.DateTimeSerializer;
import com.lonicera.context.EvalContext;
import com.lonicera.serializer.NumberSerializer;
import com.lonicera.serializer.ObjectSerializer;
import com.lonicera.serializer.StringSerializer;
import com.lonicera.serializer.TypeSerializer;
import com.lonicera.parser.ASTNode;
import com.lonicera.parser.ArrayParser;
import com.lonicera.parser.BooleanParser;
import com.lonicera.parser.JSONParser;
import com.lonicera.parser.NullParser;
import com.lonicera.parser.NumberParser;
import com.lonicera.parser.ObjectParser;
import com.lonicera.parser.StringParser;
import com.lonicera.parser.ValueParser;
import com.lonicera.token.Lexer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import lombok.Data;

/**
 * Hello world!
 */
@Data
public class App {

  public static class Tomcat {

    private Integer id;
    private String name;
    private char symbol;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    private List<Service> serviceList;

    public char getSymbol() {
      return symbol;
    }

    public void setSymbol(char symbol) {
      this.symbol = symbol;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public LocalDateTime getStartTime() {
      return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
      this.startTime = startTime;
    }

    public List<Service> getServiceList() {
      return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
      this.serviceList = serviceList;
    }
  }


  public static class Service {

    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  private static ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  public static void main(String[] args) throws JsonProcessingException, InterruptedException {


    List<Service> serviceList = new LinkedList<>();
    Service service = new Service();
    service.setName("service");
    serviceList.add(service);
    Tomcat object = new Tomcat();
    object.id = 1;
    object.name = "tomcat";
    object.symbol = 's';
    object.startTime = LocalDateTime.now();
    object.serviceList = serviceList;

    String json = objectMapper.writeValueAsString(object);

    slowJson(json);
  }

  private static void slowJson(String json) throws JsonProcessingException {

    SlowJson slowJson = new SlowJson();

    long start = System.nanoTime();
    for (int i = 0; i < 1000000; i++) {
      Tomcat tomcat0 = slowJson.readValue(json, Tomcat.class);
    }
    long end = System.nanoTime();
    System.out.println("cost1 : " + (end - start));


    start = System.nanoTime();
    for (int i = 0; i < 1000000; i++) {
      Tomcat tomcat = objectMapper.readValue(json, Tomcat.class);
    }
    end = System.nanoTime();

    System.out.println("cost2 : " + (end - start));

  }

}
