package com.lonicera;

import com.lonicera.environment.DefaultEvalEnvironment;
import com.lonicera.environment.Environment;
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

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    JSONParser jsonParser = new JSONParser();

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

    Lexer lexer = new Lexer("{\"code\":1,\"msg\":\"成功\",\"data\":[{\"id\":6685,\"name\":\"开发者中心\",\"label\":\"开发者中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"developer_center\",\"url\":\"http://192.168.0.112:20017\",\"icon\":\"icon-yuancheng\",\"likeId\":\"6685\"},{\"id\":6899,\"name\":\"交易中心租户端\",\"label\":\"交易中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"trade_center\",\"url\":\"http://192.168.0.112:20018\",\"icon\":\"icon-jiaoyi01\",\"likeId\":\"6899\"},{\"id\":7061,\"name\":\"工作台\",\"label\":\"工作台\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"workbench\",\"url\":\"http://192.168.0.112:20030\",\"icon\":\"icon-menu-home\",\"likeId\":\"7061\"},{\"id\":8030,\"name\":\"我的众包\",\"label\":\"我的众包\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"crowdsourcing\",\"url\":\"http://192.168.0.112:20021/\",\"icon\":\"icon-wulianwang\",\"likeId\":\"8030\"},{\"id\":3003,\"name\":\"用户中心\",\"label\":\"用户中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"user_center_tenant\",\"url\":\"http://192.168.0.112:20001\",\"icon\":\"icon-yonghu300\",\"likeId\":\"3003\"},{\"id\":3002,\"name\":\"授权中心\",\"label\":\"授权中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[\"app_hide\"],\"code\":\"authorization_center_tenant\",\"url\":\"http://192.168.0.112:20002\",\"icon\":\"icon-shouquan30\",\"likeId\":\"3002\"},{\"id\":3004,\"name\":\"系统管理\",\"label\":\"系统管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"system_configuration_tenant\",\"url\":\"http://192.168.0.112:20003\",\"icon\":\"icon-xitong300\",\"likeId\":\"3004\"},{\"id\":4164,\"name\":\"消息中心\",\"label\":\"消息中心\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"message_center_tenant\",\"url\":\"http://192.168.0.112:20012\",\"icon\":\"icon-icon\",\"likeId\":\"4164\"},{\"id\":6865,\"name\":\"内容管理\",\"label\":\"内容管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"content_manage\",\"url\":\"http://192.168.0.112:20008\",\"icon\":\"icon-neirong300\",\"likeId\":\"6865\"},{\"id\":6554,\"name\":\"弗雷云应用\",\"label\":\"我的应用\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"app_center\",\"url\":\"http://192.168.0.112:20014\",\"icon\":\"icon-yingyong300\",\"likeId\":\"6554\"},{\"id\":6566,\"name\":\"弗雷云概览\",\"label\":\"首页概览\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"fly_overview\",\"url\":\"http://192.168.0.112:20015\",\"icon\":\"icon-menu-home\",\"likeId\":\"6566\"},{\"id\":6631,\"name\":\"设备管理-租户端\",\"label\":\"设备管理\",\"authFlag\":1,\"appType\":1,\"funcType\":2,\"funcCategory\":-1,\"sourceType\":1,\"tags\":[],\"code\":\"equipment_management\",\"url\":\"http://192.168.0.112:20016\",\"icon\":null,\"likeId\":\"6631\"}]}");
    ASTNode astNode = jsonParser.parse(lexer);

    Environment environment = new DefaultEvalEnvironment();

    long start = System.nanoTime();
    Object jsonObject = astNode.eval(environment);
    long end = System.nanoTime();
    System.out.println(end - start);
  }

}
