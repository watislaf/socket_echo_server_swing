package Server;

import controller.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class MyServer {
    Controller controller_;
    AsynchronousServerSocketChannel serverChannel;
    ArrayList<ServerReadWrite> room = new ArrayList<>();

    public MyServer(Controller controller) throws IOException, ExecutionException, InterruptedException {
        controller_ = controller;
        serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4999);
        serverChannel.bind(hostAddress);
    }

    public void StartListen() {
        while (true) {
            serverChannel.accept(null, new
                    CompletionHandler<AsynchronousSocketChannel, Object>() {
                        @Override
                        public void completed(AsynchronousSocketChannel client, Object attachment) {
                            try {
                                serverChannel.accept(null, this);
                            } catch (Exception e) {
                                return;
                            }
                            if ((client != null) && (client.isOpen())) {
                                ServerReadWrite handler = new ServerReadWrite(client, room);
                                ByteBuffer buffer = ByteBuffer.allocate(32);
                                Map<String, Object> readInfo = new HashMap<>();
                                readInfo.put("action", "read");
                                readInfo.put("buffer", buffer);

                                client.read(buffer, readInfo, handler);
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                        }
                    });
        }

    }

}
