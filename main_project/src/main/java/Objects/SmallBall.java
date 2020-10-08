package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SmallBall extends Drawable {
    Integer angle = 0;

    public SmallBall() {
        super(new Point(0, 0), 0, ChangesEvent.smallBallCreated);
    }

    public SmallBall(Message message) {
        super(new Point(message.GetX(), message.GetY()), message.GetUniqId(), ChangesEvent.smallBallCreated);
        angle = message.GetAdditionalVar();
        radius_ = 20.;
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(related_event_, position_.x, position_.y, angle, uniq_global_id_);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Point position = new Point(position_);
        position.x -= offset.x;
        position.y -= offset.y;

        Shape theCircle;
        theCircle = new Ellipse2D.Double(position.x - radius_,
                position.y - radius_, 2.0 * radius_, 2.0 * radius_);
        g2d.draw(theCircle);
        theCircle = new Ellipse2D.Double(position.x - radius_ * animation_ / 100,
                position.y - radius_, 2.0 * radius_ * animation_ / 100, 2.0 * radius_);
        g2d.draw(theCircle);

    }

    @Override
    public void Tick() {
        double x = 5 * Math.cos(angle * Math.PI / 180);
        double y = 5 * Math.sin(angle * Math.PI / 180);
        position_.x += x;
        position_.y += y;

        animation_ += animation_step_;
        if (animation_ > 100) {
            animation_step_ = -5;
        }
        if (animation_ < 0) {
            animation_step_ = 5;
        }
    }

}
