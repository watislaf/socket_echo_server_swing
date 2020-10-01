package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Boom extends Drawable {
    Double radius = 30.;

    public Boom(Message message) {
        super(new Point(message.getX(), message.getY()), message.getUniqId());
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(ChangesEvent.BoomCreated, position_.x, position_.y, 0, uniq_global_id);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Point offsetted_position = new Point(position_);
        offsetted_position.x -= offset.x;
        offsetted_position.y -= offset.y;
        Point2D center = new Point2D.Float(offsetted_position.x, offsetted_position.y);
        float[] dist = {0.25f, .75f};
        Color[] colors = {Color.WHITE, Color.red.darker()};
        RadialGradientPaint paint = new RadialGradientPaint(center, radius.floatValue(), dist,
                colors, MultipleGradientPaint.CycleMethod.REFLECT);
        g2d.setPaint(paint);
        Shape theCircle;
        theCircle = new Ellipse2D.Double(offsetted_position.x - radius,
                offsetted_position.y - radius, 2.0 * radius, 2.0 * radius);
        g2d.fill(theCircle);
    }

    @Override
    public void Tick() {
        radius -= 9;
        if (radius <= 0) {
            radius = 1.;
            is_dead_ = true;
        }
    }

}
