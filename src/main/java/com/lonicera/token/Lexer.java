package com.lonicera.token;

import com.lonicera.exception.ReadAlreadyReachEndException;
import com.lonicera.exception.UnRecognizeTokenException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public final class Lexer {

  private StringReader reader;
  private String expr;
  private int readerIndex = 0;
  private Deque<Integer> unReadCharStack;
  private LinkedList<Token> lookAheadQueue;
  private boolean readReachEnd = false;

  public Lexer(String expr) {
    this.expr = expr;
    reader = new StringReader(expr);
    unReadCharStack = new LinkedList<>();
    lookAheadQueue = new LinkedList<>();
  }

  public Token read() {
    if (lookAheadQueue.size() > 0) {
      return lookAheadQueue.removeFirst();
    }
    return directRead();
  }

  private Token directRead() {

    int c = readChar();
    while (isSpace(c)) {
      c = readChar();
    }

    if (isSkipChar((char)c)) {
      return new SkipToken((char)c, readerIndex - 1);
    }

    StringBuilder sb = new StringBuilder();
    if (isNegative(c) || isDigit(c)) {
      sb.append((char)c);
      c = readChar();
      while (isDigit(c)) {
        sb.append((char)c);
        c = readChar();
      }
      if (isDot(c)) {
        sb.append((char)c);
        c = readChar();
        while (isDigit(c)) {
          sb.append((char)c);
          c = readChar();
        }
      }
      if (isExpect(c, 'E') || isExpect(c, 'e')) {
        sb.append((char)c);
        c = readChar();
        if (isExpect(c, '-') || isExpect(c, '+')) {
          sb.append((char)c);
        }
        c = readChar();
        if (isDigit(c)) {
          sb.append((char)c);
          c = readChar();
          while (isDigit(c)) {
            sb.append((char)c);
            c = readChar();
          }
          unReadChar(c);
        } else {
          unReadChar(c);
        }
        sb.append((char)c);
        throw new UnRecognizeTokenException(sb.toString());
      } else {
        unReadChar(c);
      }
      return new NumberToken(sb.toString(), readerIndex - sb.length());
    } else if (isDoubleQuotes(c)) {
      int lastChar = c;
      while (!isDoubleQuotes(c = readChar())) {
        if (c < 0) {
          throwUnRecognizeTokenException();
        }
        if (isSlash(c)) {
          sb.append((char)c);
          String escape = readEscapeChar();
          sb.append(escape);
        } else {
          sb.append((char) c);
        }
      }
      return new StringToken(sb.toString(), readerIndex - sb.length());
    } else if (isExpect(c, 'n')) {
      if (readChar() == 'u' && readChar() == 'l' && readChar() == 'l') {
        return new NullToken("null", readerIndex - 4);
      }
    } else if (isExpect(c, 't') || isExpect(c, 'f')) {
      return new BooleanToken(sb.toString(), readerIndex - sb.length());
    }
    if (c > -1) {
      throwUnRecognizeTokenException();
    }
    if (readReachEnd) {
      throw new ReadAlreadyReachEndException();
    }
    readReachEnd = true;
    return Token.EOF;
  }

  private static final Set<Character> ESCAPE_START_CHAR_SET;

  static {
    Set<Character> charSet = new HashSet<>();
    charSet.add('"');
    charSet.add('\\');
    charSet.add('/');
    charSet.add('b');
    charSet.add('f');
    charSet.add('n');
    charSet.add('r');
    charSet.add('t');
    ESCAPE_START_CHAR_SET = Collections.unmodifiableSet(charSet);
  }

  private String readEscapeChar() {
    StringBuilder sb = new StringBuilder();
    int more =  readChar();
    if (ESCAPE_START_CHAR_SET.contains(more)) {
      return sb.append(more).toString();
    }
    if (more == 'u') {
      sb.append(more);
      for (int i = 0; i < 4; i++) {
        more =  readChar();
        if (isHex(more)) {
          sb.append(more);
        } else {
          sb.append(more);
          throw new UnRecognizeTokenException("\\" + sb.toString());
        }
      }
    }
    throw new UnRecognizeTokenException("\\" + more);
  }

  private boolean isHex(int more) {
    return (more >= '0' && more <= '9') || (more >= 'A' && more <= 'F') || (more >= 'a'
        && more <= 'f');
  }

  private boolean isExpect(int present, char expect) {
    return present == expect;
  }

  private void throwUnRecognizeTokenException() {
    throw new UnRecognizeTokenException(expr.substring(0, readerIndex));
  }

  private static boolean isDot(int c) {
    return c == '.';
  }

  private static boolean isSlash(int c) {
    return c == '\\';
  }

  private static boolean isDoubleQuotes(int c) {
    return c == '"';
  }

  private boolean isNegative(int c) {
    return c == '-';
  }

  private boolean isSpace(int c) {
    return c == ' ' || c == '\t' || c == '\r' || c == '\n';
  }

  private static boolean isDigit(int c) {
    return c >= '0' && c <= '9';
  }

  private static Set<Character> SKIP_CHAR_SET = new HashSet<>();

  static {
    SKIP_CHAR_SET.add('{');
    SKIP_CHAR_SET.add('}');
    SKIP_CHAR_SET.add(':');
    SKIP_CHAR_SET.add('[');
    SKIP_CHAR_SET.add(']');
    SKIP_CHAR_SET.add(',');
  }

  private boolean isSkipChar(char c) {
    return SKIP_CHAR_SET.contains(c);
  }

  private int readChar() {
    try {
      if (unReadCharStack.size() > 0) {
        readerIndex++;
        return unReadCharStack.removeLast();
      }

      int c = reader.read();
      if (c > 0) {
        readerIndex++;
      }
      return c;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private void unReadChar(int c) {
    readerIndex--;
    unReadCharStack.offerLast(c);
  }

  public Token lookAhead(int k) {
    if (k < 1) {
      throw new IllegalArgumentException("k must > 0");
    }

    while (lookAheadQueue.size() < k) {
      Token token = directRead();
      lookAheadQueue.offer(token);
    }
    int index = k - 1;
    return lookAheadQueue.get(index);
  }

  public Token peek() {
    return lookAhead(1);
  }

  public String expr() {
    return expr;
  }

  public static void main(String[] args) {
    Lexer lexer = new Lexer(
        "{\"code\":1,\"msg\":\"成功\",\"data\":[{\"id\":6685,\"name\":\"开发者中心\",\"label\":\"开发者中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"developer_center\",\"url\":\"http://192.168.0.112:20017\",\"icon\":\"icon-yuancheng\",\"likeId\":\"6685\"},{\"id\":6899,\"name\":\"交易中心租户端\",\"label\":\"交易中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"trade_center\",\"url\":\"http://192.168.0.112:20018\",\"icon\":\"icon-jiaoyi01\",\"likeId\":\"6899\"},{\"id\":7061,\"name\":\"工作台\",\"label\":\"工作台\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"workbench\",\"url\":\"http://192.168.0.112:20030\",\"icon\":\"icon-menu-home\",\"likeId\":\"7061\"},{\"id\":8030,\"name\":\"我的众包\",\"label\":\"我的众包\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"crowdsourcing\",\"url\":\"http://192.168.0.112:20021/\",\"icon\":\"icon-wulianwang\",\"likeId\":\"8030\"},{\"id\":3003,\"name\":\"用户中心\",\"label\":\"用户中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"user_center_tenant\",\"url\":\"http://192.168.0.112:20001\",\"icon\":\"icon-yonghu300\",\"likeId\":\"3003\"},{\"id\":3002,\"name\":\"授权中心\",\"label\":\"授权中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"authorization_center_tenant\",\"url\":\"http://192.168.0.112:20002\",\"icon\":\"icon-shouquan30\",\"likeId\":\"3002\"},{\"id\":3004,\"name\":\"系统管理\",\"label\":\"系统管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"system_configuration_tenant\",\"url\":\"http://192.168.0.112:20003\",\"icon\":\"icon-xitong300\",\"likeId\":\"3004\"},{\"id\":4164,\"name\":\"消息中心\",\"label\":\"消息中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"message_center_tenant\",\"url\":\"http://192.168.0.112:20012\",\"icon\":\"icon-icon\",\"likeId\":\"4164\"},{\"id\":6865,\"name\":\"内容管理\",\"label\":\"内容管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"content_manage\",\"url\":\"http://192.168.0.112:20008\",\"icon\":\"icon-neirong300\",\"likeId\":\"6865\"},{\"id\":6554,\"name\":\"弗雷云应用\",\"label\":\"我的应用\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"app_center\",\"url\":\"http://192.168.0.112:20014\",\"icon\":\"icon-yingyong300\",\"likeId\":\"6554\"},{\"id\":6566,\"name\":\"弗雷云概览\",\"label\":\"首页概览\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"fly_overview\",\"url\":\"http://192.168.0.112:20015\",\"icon\":\"icon-menu-home\",\"likeId\":\"6566\"},{\"id\":6631,\"name\":\"设备管理-租户端\",\"label\":\"设备管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"equipment_management\",\"url\":\"http://192.168.0.112:20016\",\"icon\":\"icon-shebei300\",\"likeId\":\"6631\"}]}");
    while (lexer.peek() != Token.EOF) {
      System.out.println(lexer.read());
    }
  }

}
