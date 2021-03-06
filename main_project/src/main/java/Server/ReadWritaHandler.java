package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Map;

class ServerReadWrite implements CompletionHandler<Integer, Map<String, Object>> {
    ArrayList<Message> non_send_messages_ = new ArrayList<>();
    ArrayList<ServerReadWrite> room_;
    AsynchronousSocketChannel client_;
    int index_in_room_;

    public ServerReadWrite(AsynchronousSocketChannel client, ArrayList<ServerReadWrite> room) {
        client_ = client;
        room_ = room;
        room.add(this);
        index_in_room_ = room_.size();
    }

    @Override
    public void completed(Integer result, Map<String, Object> attachment) {
        String action = (String) attachment.get("action");
        if ("read".equals(action)) {
            ByteBuffer buffer = (ByteBuffer) attachment.get("buffer");
            String string_from_client = new String(buffer.array()).trim();
            // If user left
            if (new String(buffer.array()).trim().equals("")) {
                try {
                    room_.remove(this);
                    client_.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            buffer.flip();
            attachment.put("action", "write");
            attachment.put("buffer", buffer);

            if (Message.IsNotZero(string_from_client)) {
                for (ServerReadWrite client_handler : room_) {
                    try {
                        // Some times here is an error if some one is left
                        client_handler.non_send_messages_.add(new Message(string_from_client));
                    } catch (Exception e) {
                        System.out.println("Smth went wrong");
                    }
                }
            }

            byte[] byteMsg;
            if (!non_send_messages_.isEmpty()) {
                byteMsg = non_send_messages_.get(0).toString().getBytes();
                non_send_messages_.remove(0);
            } else {
                byteMsg = Message.ToDefaultString().getBytes();
            }
            buffer = ByteBuffer.wrap(byteMsg);
            client_.write(buffer, attachment, this);

            buffer.clear();

        }
        if ("write".equals(action)) {
            // Give buffer back to user to write
            ByteBuffer buffer = ByteBuffer.allocate(32);
            attachment.put("action", "read");
            attachment.put("buffer", buffer);
            client_.read(buffer, attachment, this);
        }
    }

    @Override
    public void failed(Throwable exc, Map<String, Object> attachment) {
        System.out.println("Bye bye");
    }
}