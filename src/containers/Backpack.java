package containers;

import Objects.Drawable;

import java.util.ArrayList;

public class Backpack<T extends Drawable> {
    private ArrayList<T> shapes;
    int n_;

    public Backpack(int n) {
        n_ = n;
        shapes = new ArrayList<T>();
    }

    public Integer Size() {
        return shapes.size();
    }
    public void addShape(T shape) throws MyException {
        if(shapes.size() == n_){
            throw new MyException("Too much");
        }
        shapes.add(shape);

    }

    public Drawable removeShape() {
        if (shapes.size() == 0) {
            System.out.println("Backpack is empty");
            return null;
        } else {
            return shapes.remove(shapes.size() - 1);
        }
    }
}
