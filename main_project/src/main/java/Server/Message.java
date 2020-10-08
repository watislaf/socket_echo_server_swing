package Server;


import java.util.ArrayList;
import java.util.Collections;

public class Message {

    static final int MAX_STRING_SIZE_ = 30;
    ChangesEvent changesEvent_;
    Integer additional_var_;
    String my_message_;
    Integer uniq_id_;
    Integer privilege_ = 0;
    Integer x_;
    Integer y_;

    public Message(String message) {
        my_message_ = message;
        ArrayList<Integer> temp = ToData();
        changesEvent_ = ChangesEvent.valueOf(temp.get(0));
        x_ = temp.get(1);
        y_ = temp.get(2);
        additional_var_ = temp.get(3);
        uniq_id_ = temp.get(4);
        privilege_ = temp.get(5);
    }

    public Message(ChangesEvent changesEvent, Integer x, Integer y, Integer additional_var, Integer uniq_id) {
        additional_var_ = additional_var;
        changesEvent_ = changesEvent;
        uniq_id_ = uniq_id;
        x_ = x;
        y_ = y;
        my_message_ = toString();
    }

    public static String ToDefaultString() {
        String x = "0#0#0#0#0#0";
        x = x.concat("#");
        while (x.length() < MAX_STRING_SIZE_) {
            x = x.concat("0");
        }
        return x;
    }

    public static boolean IsNotZero(String echo) {
        return !echo.equals(ToDefaultString());
    }

    public String toString() {
        String x = changesEvent_.getValue() + "#" +
                x_.toString() + "#" + y_.toString() + "#" + additional_var_.toString() + "#"
                + uniq_id_.toString() + "#" + privilege_.toString();
        x = x.concat("#");
        while (x.length() < MAX_STRING_SIZE_) {
            x = x.concat("0");
        }
        return x;
    }

    public ArrayList<Integer> ToData() {
        String[] temp = my_message_.split("#");
        ArrayList<Integer> data = new ArrayList<>();
        Collections.addAll(data,
                Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), // type x
                Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), //  x additional
                Integer.parseInt(temp[4]), Integer.parseInt(temp[5])); // uniq eprivilage
        return data;
    }

    public Integer GetAdditionalVar() {
        return additional_var_;
    }

    public Integer GetPrivilege() {
        return privilege_;
    }

    public ChangesEvent GetEvent() {
        return changesEvent_;
    }

    public Integer GetX() {
        return x_;
    }

    public Integer GetY() {
        return y_;
    }

    public Integer GetUniqId() {
        return uniq_id_;
    }

    public void SetPrivilege(Integer privilege) {
        privilege_ = privilege;
    }
}
