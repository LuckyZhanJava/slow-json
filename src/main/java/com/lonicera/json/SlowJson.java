package com.lonicera.json;

import com.lonicera.context.EvalContext;
import com.lonicera.parser.ASTNode;
import com.lonicera.parser.ArrayParser;
import com.lonicera.parser.BooleanParser;
import com.lonicera.parser.JSONParser;
import com.lonicera.parser.NullParser;
import com.lonicera.parser.NumberParser;
import com.lonicera.parser.ObjectParser;
import com.lonicera.parser.StringParser;
import com.lonicera.parser.ValueParser;
import com.lonicera.serializer.CollectionSerializer;
import com.lonicera.serializer.DateTimeSerializer;
import com.lonicera.serializer.NumberSerializer;
import com.lonicera.serializer.ObjectSerializer;
import com.lonicera.serializer.StringSerializer;
import com.lonicera.serializer.TypeSerializer;
import com.lonicera.token.Lexer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SlowJson {

  private JSONParser jsonParser;

  private EvalContext context;

  public SlowJson() {

    jsonParser = new JSONParser();

    ValueParser valueParser = new ValueParser();
    ArrayParser arrayParser = new ArrayParser();

    ObjectParser objectParser = new ObjectParser();

    valueParser.or(
        new StringParser(),
        objectParser,
        arrayParser,
        new NumberParser(),
        new BooleanParser(),
        new NullParser()
    );

    objectParser.valueParser(valueParser);

    arrayParser.elementParser(valueParser);

    jsonParser.or(objectParser, arrayParser);

    context = evalContext();
  }

  public <T> T readValue(String json, Class<T> clazz) {
    Lexer lexer = new Lexer(json);
    ASTNode astNode = jsonParser.tryParse(lexer);
    Object jsonObject = astNode.eval(context, clazz, clazz.getAnnotations());
    return (T)jsonObject;
  }

  private EvalContext evalContext() {
    return new EvalContext() {

      private final TypeSerializer NUMBER_SERIALIZER = new NumberSerializer();
      private final TypeSerializer STRING_SERIALIZER = new StringSerializer();
      private final TypeSerializer OBJECT_SERIALIZER = new ObjectSerializer();
      private final TypeSerializer DATE_TIME_SERIALIZER = new DateTimeSerializer();
      private final TypeSerializer COLLECTION_SERIALIZER = new CollectionSerializer();


      private final List<TypeSerializer> TYPE_SERIALIZER_LIST = new ArrayList<TypeSerializer>() {
        {
          add(NUMBER_SERIALIZER);
          add(STRING_SERIALIZER);
          add(OBJECT_SERIALIZER);
          add(DATE_TIME_SERIALIZER);
          add(COLLECTION_SERIALIZER);
        }
      };


      Map<Class<?>, TypeSerializer> CLASS_SERIALIZER_MAP = new HashMap<Class<?>, TypeSerializer>() {
        {

          put(BigDecimal.class, NUMBER_SERIALIZER);
          put(BigInteger.class, NUMBER_SERIALIZER);
          put(Long.class, NUMBER_SERIALIZER);
          put(Double.class, NUMBER_SERIALIZER);
          put(Float.class, NUMBER_SERIALIZER);
          put(Integer.class, NUMBER_SERIALIZER);
          put(Short.class, NUMBER_SERIALIZER);
          put(Byte.class, NUMBER_SERIALIZER);

          put(long.class, NUMBER_SERIALIZER);
          put(double.class, NUMBER_SERIALIZER);
          put(float.class, NUMBER_SERIALIZER);
          put(int.class, NUMBER_SERIALIZER);
          put(short.class, NUMBER_SERIALIZER);
          put(byte.class, NUMBER_SERIALIZER);

          put(String.class, STRING_SERIALIZER);
          put(Character.class, STRING_SERIALIZER);
          put(char.class, STRING_SERIALIZER);

          put(Object.class, OBJECT_SERIALIZER);

          put(LocalDateTime.class, DATE_TIME_SERIALIZER);

          put(List.class, COLLECTION_SERIALIZER);
          put(Set.class, COLLECTION_SERIALIZER);
          put(Queue.class, COLLECTION_SERIALIZER);
          put(Deque.class, COLLECTION_SERIALIZER);

        }
      };

      @Override
      public TypeSerializer getTypeSerializer(Class<?> clazz) {
        TypeSerializer existsSerializer = CLASS_SERIALIZER_MAP.get(clazz);
        if (existsSerializer != null) {
          return existsSerializer;
        }
        for (TypeSerializer serializer : TYPE_SERIALIZER_LIST) {
          if (serializer.supportTarget(clazz)) {
            CLASS_SERIALIZER_MAP.put(clazz, serializer);
            return serializer;
          }
        }
        return null;
      }
    };
  }
}
