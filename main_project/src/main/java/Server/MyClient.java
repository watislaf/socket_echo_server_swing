package Server;

import controller.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MyClient {
    AsynchronousSocketChannel client_;
    Controller controller_;

    public MyClient(Controller controller) {
        controller_ = controller;
        try {
            client_ = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            System.out.println(e);
        }
        client_.connect(new InetSocketAddress("localhost", 4999));
    }

    public void Tick() {
        SendMessage(new Message(Message.ToDefaultString()));
    }


    public void SendMessage(Message message) {
        // WRITE
        byte[] bytemsg = message.toString().getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytemsg);

        Future<Integer> writeResult;
        try { // Closed connection
            writeResult = client_.write(buffer);
        } catch (Exception e) {
            controller_.InitializeConnection();
            return;
        }
        try {
            writeResult.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            controller_.InitializeConnection();
            return;
        }
        buffer.flip();

        //  READ
        bytemsg = Message.ToDefaultString().getBytes();
        buffer = ByteBuffer.wrap(bytemsg);
        Future<Integer> readResult = client_.read(buffer);

        try {
            readResult.get(1, TimeUnit.SECONDS); //  Wait until read end
        } catch (Exception e) {
            controller_.InitializeConnection();
            return;
        }
        ////////////////////////////// Move To controller unread
        String echo = new String(buffer.array()).trim();
        buffer.clear();
        if (Message.IsNotZero(echo)) {
            controller_.AddNewUnreadMessage(new Message(echo));
        }
    }
}
