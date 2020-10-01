package Server;


import java.util.ArrayList;
import java.util.Collections;

public class Message {

    static final int MAX_STRING_SIZE_ = 30;
    String my_message;
    ChangesEvent changesEvent_ = ChangesEvent.test;
    Integer x_ = 0;
    Integer y_ = 0;
    Integer additional_var_ = 0;
    Integer uniq_id_ = 0;
    Integer privilage_ = 0;

    public Message(String message) {
        my_message = message;
        ArrayList<Integer> temp = ToData();
        changesEvent_ = ChangesEvent.valueOf(temp.get(0));
        x_ = temp.get(1);
        y_ = temp.get(2);
        additional_var_ = temp.get(3);
        uniq_id_ = temp.get(4);
        privilage_ = temp.get(5);
    }

    public Message(ChangesEvent changesEvent, Integer x, Integer y, Integer additional_var, Integer uniq_id) {
        changesEvent_ = changesEvent;
        x_ = x;
        y_ = y;
        additional_var_ = additional_var;
        uniq_id_ = uniq_id;
        my_message = toString();
    }

    public static String ToDefaultString() {
        String x = new String("0#0#0#0#0#0");
        if (x.length() < MAX_STRING_SIZE_) {
            x = x.concat("#");
            while (x.length() < MAX_STRING_SIZE_) {
                x = x.concat("0");
            }
        }
        return x;
    }

    public static boolean IsZero(String echo) {
        return echo.equals(ToDefaultString());
    }

    public String toString() {
        String x = changesEvent_.getValue() + "#" +
                x_.toString() + "#" + y_.toString() + "#" + additional_var_.toString() + "#"
                + uniq_id_.toString() + "#" + privilage_.toString();
        if (x.length() < MAX_STRING_SIZE_) {
            x = x.concat("#");
            while (x.length() < MAX_STRING_SIZE_) {
                x = x.concat("0");
            }
        }
        return x;
    }

    public ArrayList<Integer> ToData() {
        String[] temp = my_message.split("#");
        ArrayList<Integer> data = new ArrayList<>();
        Collections.addAll(data,
                Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), // type x
                Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), //  x additional
                Integer.parseInt(temp[4]), Integer.parseInt(temp[5])); // uniq privilage
        return data;

    }

    public Integer getAdditionalVar() {
        return additional_var_;
    }

    public Integer getPrivilage() {
        return privilage_;
    }

    public ChangesEvent getEvent() {
        return changesEvent_;
    }

    public Integer getX() {
        return x_;
    }

    public Integer getY() {
        return y_;
    }


    public void setPrivilage(Integer privilage) {
        privilage_ = privilage;
    }

    public Integer getUniqId() {
        return uniq_id_;
    }

    public void setUniqId(Integer uniqId) {
        uniq_id_ = uniqId;
    }

}
