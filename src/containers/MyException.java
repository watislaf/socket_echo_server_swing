package containers;

public class MyException extends Exception {
    String message_;

    MyException(String message) {
        message_ = message;
    }

    @Override
    public String toString() {
        return "MyExсeption{" +
                "message_='" + message_ + '\'' +
                '}';
    }
}
