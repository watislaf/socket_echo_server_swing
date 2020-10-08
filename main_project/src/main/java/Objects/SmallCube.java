package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;

public class SmallCube extends Drawable {
    Integer angle = 0;

    public SmallCube(Message message) {
        super(new Point(message.GetX(), message.GetY()), message.GetUniqId(), ChangesEvent.smallCubeCreated);
        angle = message.GetAdditionalVar();
        radius_ = 20.;
    }

    public SmallCube() {
        super(new Point(0, 0), 0, ChangesEvent.smallCubeCreated);
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(ChangesEvent.smallCubeCreated, position_.x, position_.y, angle, uniq_global_id_);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Point position = new Point(position_);
        position.x -= offset.x;
        position.y -= offset.y;

        int f = 4;
        for (int i = 0; i < f; i++) {
            double deg = (animation_ + i * 360 / f) * Math.PI / 180;
            int c = (int) (Math.cos(deg) * radius_);
            int s = (int) (Math.sin(deg) * radius_);

            deg = deg = (animation_ + (i + 1) * 360 / f) * Math.PI / 180;
            int c2 = (int) (Math.cos(deg) * (radius_));
            int s2 = (int) (Math.sin(deg) * (radius_));
            g2d.drawLine(position.x + c, position.y + s, position.x + c2, position.y + s2);

        }


    }

    @Override
    public void Tick() {
        double x = 5 * Math.cos(angle * Math.PI / 180);
        double y = 5 * Math.sin(angle * Math.PI / 180);
        position_.x += x;
        position_.y += y;

        animation_ += animation_step_;
        if (animation_ > 360) {
            animation_step_ = -8;
        }
        if (animation_ < 0) {
            animation_step_ = 8;
        }
    }

}
