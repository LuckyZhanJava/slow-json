package com.lonicera.app;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author LiBowei
 * @description
 * @date 2020年-11月-23日
 **/
public class App {

  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel channel = ServerSocketChannel.open();
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_CONNECT);
  }
}
