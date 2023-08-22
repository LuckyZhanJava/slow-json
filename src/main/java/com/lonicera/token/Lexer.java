package com.lonicera.token;

import com.lonicera.exception.UnRecognizeTokenException;

public final class Lexer {

  private String expr;
  private int readerIndex = 0;
  private char[] chars;
  private Token[] tokenQueue;
  private int nextTokenIndex = -1;
  private int startIndex;
  private int tokenLength;

  public Lexer(String expr) {
    this.expr = expr;
    chars = expr.toCharArray();
    tokenQueue = new Token[1];
  }

  private int readChar() {
    int c;
    if (readerIndex < chars.length) {
      c = chars[readerIndex];
      readerIndex++;
    } else {
      c = -1;
    }
    return c;
  }

  public Token read() {
    // 1. read look ahead queue
    if (nextTokenIndex >= 0) {
      Token next = tokenQueue[nextTokenIndex];
      nextTokenIndex--;
      return next;
    }

    startIndex = readerIndex;
    tokenLength = 0;

    // 2. direct read
    int c = readChar();
    while (c == ' ' || c == '\t' || c == '\r' || c == '\n'/*isSpace(c)*/) {
      c = readChar();
    }

    // skip char
    if (c == '{'
        || c == '}'
        || c == ':'
        || c == ','
        || c == '['
        || c == ']'
    ) {
      return new SkipToken(chars, startIndex);
    }

    // number
    if (c == '-' || (c >= '0' && c <= '9')) {
      tokenLength++;
      c = readChar();
      while ((c >= '0' && c <= '9')) {
        tokenLength++;
        c = readChar();
      }
      if (c == '.') {
        tokenLength++;
        c = readChar();
        while ((c >= '0' && c <= '9')) {
          tokenLength++;
          c = readChar();
        }
      }
      if (c == 'E' || c == 'e') {
        tokenLength++;
        ;
        c = readChar();
        if ((c == '-') || (c == '+')) {
          tokenLength++;
        }
        c = readChar();
        if (c >= '0' && c <= '9') {
          tokenLength++;
          ;
          c = readChar();
          while (c >= '0' && c <= '9') {
            tokenLength++;
            c = readChar();
          }
          unReadChar();
        } else {
          unReadChar();
        }
        tokenLength++;
        throw new UnRecognizeTokenException(chars, startIndex, tokenLength);
      } else {
        unReadChar();
      }
      return new NumberToken(chars, startIndex, tokenLength);
    } else if (c == '"') {
      StringBuilder sb = new StringBuilder();
      while ((c = readChar()) != '"') {
        if (c < 0) {
          throw new UnRecognizeTokenException(chars, startIndex, tokenLength);
        }
        if (c == '\\') {
          tokenLength++;
          int more = readChar();
          if(more == 'u') {
            char unicode = readUnicde();
            sb.append(unicode);
            tokenLength += 5;
          }else if(more < ESCAPE_CHARS.length && ESCAPE_CHARS[more] != 0){
            sb.append(ESCAPE_CHARS[more]);
            tokenLength += 1;
          }
        } else {
          sb.append((char)c);
          tokenLength++;
        }
      }
      return new StringToken(chars, startIndex + 1, tokenLength, sb.toString());
    } else if (c == 'n') {
      if (readChar() == 'u' && readChar() == 'l' && readChar() == 'l') {
        return new NullToken(chars, startIndex);
      }
    } else if (c == 't' || c == 'f') {
      if (c == 't'
          && readChar() == 'r'
          && readChar() == 'u'
          && readChar() == 'e') {
        return new BooleanToken(chars, startIndex, 4);
      }
      if (c == 'f'
          && readChar() == 'a'
          && readChar() == 'l'
          && readChar() == 's'
          && readChar() == 'e') {
        return new BooleanToken(chars, startIndex, 5);
      }
      throw new UnRecognizeTokenException(chars, startIndex, readerIndex - startIndex);
    }

    if (c == -1) {
      return Token.EOF;
    }
    throw new UnRecognizeTokenException(chars, startIndex, tokenLength);
  }


  private static final char[] ESCAPE_CHARS = new char[128];

  static {
    ESCAPE_CHARS['"'] = '\"';
    ESCAPE_CHARS['\\'] = '\\';
    ESCAPE_CHARS['/'] = '/';
    ESCAPE_CHARS['b'] = '\b';
    ESCAPE_CHARS['f'] = '\f';
    ESCAPE_CHARS['n'] = '\n';
    ESCAPE_CHARS['r'] = '\r';
    ESCAPE_CHARS['t'] = '\t';
  }

  private char readUnicde() {
    int value = 0;
    for (int i = 0; i < 4; i++) {
      int more = readChar();
      if (isHex(more)) {
        value += (HEXS[more] << ((3 - i) * 4));
      } else {
        throw new UnRecognizeTokenException(chars, startIndex, tokenLength);
      }
    }
    return (char) value;
  }

  private static final int[] HEXS = new int[123];
  static {
    int startVal = 0;
    for(int i = '0'; i <= '9'; i++){
      HEXS[i] = startVal;
      startVal++;
    }

    startVal = 10;
    for(int i = 'A'; i <= 'F'; i++){
      HEXS[i] = startVal;
      startVal++;
    }

    startVal = 10;
    for(int i = 'a'; i <= 'f'; i++){
      HEXS[i] = startVal;
      startVal++;
    }

  }

  private boolean isHex(int more) {
    return (more >= '0' && more <= '9') || (more >= 'A' && more <= 'F') || (more >= 'a'
        && more <= 'f');
  }

  private void unReadChar() {
    readerIndex--;
  }

  public Token lookAhead(int k) {
    if (k < 1 || k > 1) {
      throw new IllegalArgumentException("k must eq 1");
    }

    while (nextTokenIndex < k - 1) {
      Token token = read();
      tokenQueue[nextTokenIndex + 1] = token;
      nextTokenIndex += 1;
    }
    return tokenQueue[k - 1];
  }

  public Token peek() {
    return lookAhead(1);
  }

  public String expr() {
    return expr;
  }

  public static void main(String[] args) {
    Lexer lexer = new Lexer(
        "{\"code\":1,\"msg\":\"成功\",\"data\":[{\"id\":6685,\"name\":\"开发者中心\",\"label\":\"开发者中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"developer_center\",\"url\":\"http://192.168.0.112:20017\",\"icon\":\"icon-yuancheng\",\"likeId\":\"6685\"},{\"id\":6899,\"name\":\"交易中心租户端\",\"label\":\"交易中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"trade_center\",\"url\":\"http://192.168.0.112:20018\",\"icon\":\"icon-jiaoyi01\",\"likeId\":\"6899\"},{\"id\":7061,\"name\":\"工作台\",\"label\":\"工作台\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"workbench\",\"url\":\"http://192.168.0.112:20030\",\"icon\":\"icon-menu-home\",\"likeId\":\"7061\"},{\"id\":8030,\"name\":\"我的众包\",\"label\":\"我的众包\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"crowdsourcing\",\"url\":\"http://192.168.0.112:20021/\",\"icon\":\"icon-wulianwang\",\"likeId\":\"8030\"},{\"id\":3003,\"name\":\"用户中心\",\"label\":\"用户中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"user_center_tenant\",\"url\":\"http://192.168.0.112:20001\",\"icon\":\"icon-yonghu300\",\"likeId\":\"3003\"},{\"id\":3002,\"name\":\"授权中心\",\"label\":\"授权中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"authorization_center_tenant\",\"url\":\"http://192.168.0.112:20002\",\"icon\":\"icon-shouquan30\",\"likeId\":\"3002\"},{\"id\":3004,\"name\":\"系统管理\",\"label\":\"系统管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"system_configuration_tenant\",\"url\":\"http://192.168.0.112:20003\",\"icon\":\"icon-xitong300\",\"likeId\":\"3004\"},{\"id\":4164,\"name\":\"消息中心\",\"label\":\"消息中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"message_center_tenant\",\"url\":\"http://192.168.0.112:20012\",\"icon\":\"icon-icon\",\"likeId\":\"4164\"},{\"id\":6865,\"name\":\"内容管理\",\"label\":\"内容管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"content_manage\",\"url\":\"http://192.168.0.112:20008\",\"icon\":\"icon-neirong300\",\"likeId\":\"6865\"},{\"id\":6554,\"name\":\"弗雷云应用\",\"label\":\"我的应用\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"app_center\",\"url\":\"http://192.168.0.112:20014\",\"icon\":\"icon-yingyong300\",\"likeId\":\"6554\"},{\"id\":6566,\"name\":\"弗雷云概览\",\"label\":\"首页概览\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"fly_overview\",\"url\":\"http://192.168.0.112:20015\",\"icon\":\"icon-menu-home\",\"likeId\":\"6566\"},{\"id\":6631,\"name\":\"设备管理-租户端\",\"label\":\"设备管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"equipment_management\",\"url\":\"http://192.168.0.112:20016\",\"icon\":\"icon-shebei300\",\"likeId\":\"6631\"}]}"
    );

    while (lexer.peek() != Token.EOF) {
      Token token = lexer.read();
      System.out.println(token);
    }
  }

}
