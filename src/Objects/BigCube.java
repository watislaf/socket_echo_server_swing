package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BigCube extends Drawable {

    public BigCube(Message message) {
        super(new Point(message.GetX(), message.GetY()), message.GetUniqId(),ChangesEvent.bigCubeCreated);
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(related_event_, position_.x, position_.y, 0, uniq_global_id_);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Shape theSquare;
        Point position = new Point(position_);
        position.x -= offset.x;
        position.y -= offset.y;
        theSquare = new Rectangle.Double(position.x - radius_,
                position.y - radius_, 2.0 * radius_, 2.0 * radius_);
        g2d.draw(theSquare);

        for (int i = -5; i < 20; i += 6) {
            Integer animation = radius_.intValue() * ((animation_ + i + 30) % 100) / 70;
            g2d.drawLine(position.x - animation, position.y - radius_.intValue(),
                    position.x + radius_.intValue(),
                    position.y + radius_.intValue());

            g2d.drawLine(position.x + animation, position.y + radius_.intValue(),
                    position.x - radius_.intValue(),
                    position.y - radius_.intValue());

            g2d.drawLine(position.x + radius_.intValue(), position.y - animation,
                    position.x - radius_.intValue(),
                    position.y + radius_.intValue());

            g2d.drawLine(position.x - radius_.intValue(), position.y + animation,
                    position.x + radius_.intValue(),
                    position.y - radius_.intValue());

        }


        g2d.setColor(Color.gray);
        Shape theCircle = new Ellipse2D.Double(position.x - 100,
                position.y - 100, 200, 200);
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
