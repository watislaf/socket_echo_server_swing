package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SmallBall extends Drawable {
    Double radius = 20.;
    Integer angular = 0;

    public SmallBall() {
        super(new Point(0, 0), 0);
    }

    public SmallBall(Message message) {
        super(new Point(message.getX(), message.getY()), message.getUniqId());
        angular = message.getAdditionalVar();
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(ChangesEvent.smallBallCreated, position_.x, position_.y, angular, uniq_global_id);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Point offsetted_position = new Point(position_);
        offsetted_position.x -= offset.x;
        offsetted_position.y -= offset.y;


        Shape theCircle;
        theCircle = new Ellipse2D.Double(offsetted_position.x - radius,
                offsetted_position.y - radius, 2.0 * radius, 2.0 * radius);
        g2d.draw(theCircle);
        theCircle = new Ellipse2D.Double(offsetted_position.x - radius * animation_ / 100,
                offsetted_position.y - radius, 2.0 * radius * animation_ / 100, 2.0 * radius);
        g2d.draw(theCircle);

    }

    @Override
    public void Tick() {
        double x = 5 * Math.cos(angular * Math.PI / 180);
        double y = 5 * Math.sin(angular * Math.PI / 180);
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
