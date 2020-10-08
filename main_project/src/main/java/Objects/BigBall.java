package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BigBall extends Drawable {

    public BigBall(Message message) {
        super(new Point(message.GetX(), message.GetY()), message.GetUniqId(),ChangesEvent.bigBallCreated);
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(related_event_, position_.x, position_.y, 0, uniq_global_id_);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Shape theCircle;
        Point offsetted_position = new Point(position_);
        offsetted_position.x -= offset.x;
        offsetted_position.y -= offset.y;

        theCircle = new Ellipse2D.Double(offsetted_position.x - radius_,
                offsetted_position.y - radius_, 2.0 * radius_, 2.0 * radius_);
        g2d.draw(theCircle);

        for (int i = 1; i < 76; i += 30) {
            theCircle = new Ellipse2D.Double(offsetted_position.x - radius_ * ((animation_ + i) % 100) / (100),
                    offsetted_position.y - radius_, 2.0 * radius_ * ((i + animation_) % 100) / (100), 2.0 * radius_);
            g2d.draw(theCircle);
        }

        g2d.setColor(Color.gray);
        theCircle = new Ellipse2D.Double(offsetted_position.x - 100,
                offsetted_position.y - 100, 200, 200);
        g2d.draw(theCircle);

    }

    @Override
    public void Tick() {
        animation_ += animation_step_;
        if (animation_ > 100) {
            animation_step_ = -5;
        }
        if (animation_ < 0) {
            animation_step_ = 5;
        }
    }

}
