package containers;

public class BackPackException extends Exception {
    String message_;

    BackPackException(String message) {
        message_ = message;
    }

    @Override
    public String toString() {
        return "MyExсeption{" + "message_='" + message_ + "'}";
    }
}
