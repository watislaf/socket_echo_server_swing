package Server;

import controller.Controller;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyServer {
    int server_status_ = 404;
    Controller controller_;
    AsynchronousServerSocketChannel serverChannel;
    ArrayList<ServerReadWrite> room = new ArrayList<>();

    public MyServer(Controller controller) {
        controller_ = controller;
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4999);
            serverChannel.bind(hostAddress);
            StartListen();
        } catch (java.nio.channels.AcceptPendingException io) {
            server_status_ = 200;
        } catch (Exception ignored) {
        }
    }

    public int GetStatus() {
        return server_status_;
    }

    void StartListen() {
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
                    }
            );
        }
    }
}
