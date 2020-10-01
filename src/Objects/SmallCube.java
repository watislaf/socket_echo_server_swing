package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;

public class SmallCube extends Drawable {
    Double radius = 20.;
    Integer angular = 0;

    public SmallCube(Message message) {
        super(new Point(message.getX(), message.getY()), message.getUniqId());
        angular = message.getAdditionalVar();
    }

    public SmallCube() {
        super(new Point(0, 0), 0);
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(ChangesEvent.smallCubeCreated, position_.x, position_.y, angular, uniq_global_id);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Point offsetted_position = new Point(position_);
        offsetted_position.x -= offset.x;
        offsetted_position.y -= offset.y;

        int f = 4;
        for (int i = 0; i < f; i++) {
            Double deg = (animation_ + i * 360 / f) * Math.PI / 180;
            Integer c = (int) (Math.cos(deg) * radius);
            Integer s = (int) (Math.sin(deg) * radius);

            deg = deg = (animation_ + (i + 1) * 360 / f) * Math.PI / 180;
            Integer c2 = (int) (Math.cos(deg) * (radius));
            Integer s2 = (int) (Math.sin(deg) * (radius));
            g2d.drawLine(offsetted_position.x + c, offsetted_position.y + s, offsetted_position.x + c2, offsetted_position.y + s2);

        }


    }

    @Override
    public void Tick() {
        double x = 5 * Math.cos(angular * Math.PI / 180);
        double y = 5 * Math.sin(angular * Math.PI / 180);
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
