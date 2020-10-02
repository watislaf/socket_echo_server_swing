package containers;

import Objects.Drawable;

import java.util.ArrayList;

public class Backpack<T extends Drawable> {
    private ArrayList<T> shapes_;
    private int max_number_;

    public Backpack(int n) {
        shapes_ = new ArrayList<T>();
        max_number_ = n;
    }

    public void AddShape(T shape) throws BackPackException {
        if (Size() == max_number_) {
            throw new BackPackException("Too much");
        }
        shapes_.add(shape);
    }

    public Drawable RemoveShape() {
        return shapes_.remove(shapes_.size() - 1);
    }

    public Integer Size() {
        return shapes_.size();
    }
}
