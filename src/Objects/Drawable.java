package Objects;

import Server.Message;

import java.awt.*;

public abstract class Drawable {
    Point position_;

    Integer uniq_global_id;
    boolean is_dead_;
    int animation_ = -40;
    int animation_step_ = 15;
    Color main_color_ = Color.white;

    Drawable(Point x, Integer uniq) {
        position_ = new Point(x.x, x.y);
        uniq_global_id = uniq;
    }

    public Double Distance(Drawable to) {
        return Math.sqrt(Math.pow(position_.x - to.position_.x, 2) + Math.pow(position_.y - to.position_.y, 2));
    }

    public void SetMainColor(Color main_color) {
        main_color_ = main_color;
    }

    public Integer GetUniqId() {
        return uniq_global_id;
    }

    public abstract void Draw(Graphics2D g2d, Point offset);

    public abstract void Tick();

    public abstract Message PackToMessage(Integer additionalVar);

    public Point GetPosition() {
        return position_;
    }

    public boolean IsDead() {
        if (is_dead_ == true) {
            return true;
        }
        if (position_.x < -100. || position_.x > 2000.) {
            return true;
        }
        return position_.y < -100. || position_.y > 1200.;
    }

    public  void ReloadAnimation(){
        animation_ = -40;
        animation_step_ = 15;
    }
}
