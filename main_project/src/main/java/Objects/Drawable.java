package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;

public abstract class Drawable {
    Color main_color_ = Color.white;
    ChangesEvent related_event_;
    Integer uniq_global_id_;
    int animation_step_ = 15;
    int animation_ = -40;
    Double radius_ = 50.;
    boolean is_dead_;
    Point position_;

    Drawable(Point x, Integer uniq, ChangesEvent event) {
        position_ = new Point(x.x, x.y);
        uniq_global_id_ = uniq;
        related_event_ = event;
    }

    public void ReloadAnimation() {
        animation_step_ = 15;
        animation_ = 0;
    }

    public boolean IsDead() {
        if (is_dead_ || position_.x < -100. || position_.x > 2000.) {
            return true;
        }
        return position_.y < -100. || position_.y > 1200.;
    }

    public Point GetPosition() {
        return position_;
    }

    public Double Distance(Drawable to) {
        return Math.sqrt(Math.pow(position_.x - to.position_.x, 2) + Math.pow(position_.y - to.position_.y, 2));
    }

    public void SetMainColor(Color main_color) {
        main_color_ = main_color;
    }

    public Integer GetUniqId() {
        return uniq_global_id_;
    }

    public abstract Message PackToMessage(Integer additionalVar);

    public abstract void Draw(Graphics2D g2d, Point offset);

    public abstract void Tick();
}
