package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BigCube extends Drawable {
    Double radius = 50.;

    public BigCube(Message message) {
        super(new Point(message.getX(), message.getY()), message.getUniqId());
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(ChangesEvent.bigCubeCreated, position_.x, position_.y, 0, uniq_global_id);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Shape theSquare;
        Point true_position = new Point(position_);
        true_position.x -= offset.x;
        true_position.y -= offset.y;
        theSquare = new Rectangle.Double(true_position.x - radius,
                true_position.y - radius, 2.0 * radius, 2.0 * radius);
        g2d.draw(theSquare);

        for (int i = -5; i < 20; i += 6) {
            Integer animation = radius.intValue() * ((animation_ + i + 30) % 100) / 70;
            g2d.drawLine(true_position.x - animation, true_position.y - radius.intValue(),
                    true_position.x + radius.intValue(),
                    true_position.y + radius.intValue());

            g2d.drawLine(true_position.x + animation, true_position.y + radius.intValue(),
                    true_position.x - radius.intValue(),
                    true_position.y - radius.intValue());

            g2d.drawLine(true_position.x + radius.intValue(), true_position.y - animation,
                    true_position.x - radius.intValue(),
                    true_position.y + radius.intValue());

            g2d.drawLine(true_position.x - radius.intValue(), true_position.y + animation,
                    true_position.x + radius.intValue(),
                    true_position.y - radius.intValue());

        }


        g2d.setColor(Color.gray);
        Shape theCircle = new Ellipse2D.Double(true_position.x - 100,
                true_position.y - 100, 200, 200);
        g2d.draw(theCircle);
    }

    @Override
    public void Tick() {
        animation_ += animation_step_;
        if (animation_ >= 100) {
            animation_step_ = -5;
        }
        if (animation_ <= -183) {
            animation_step_ = 10;
        }
    }
}
