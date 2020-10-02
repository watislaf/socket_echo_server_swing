package Objects;

import Server.ChangesEvent;
import Server.Message;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Boom extends Drawable {

    public Boom(Message message) {
        super(new Point(message.GetX(), message.GetY()), message.GetUniqId(),ChangesEvent.boomCreated);
        radius_ = 30.;
    }

    @Override
    public Message PackToMessage(Integer additionalVar) {
        return new Message(related_event_, position_.x, position_.y, 0, uniq_global_id_);
    }

    @Override
    public void Draw(Graphics2D g2d, Point offset) {
        g2d.setColor(main_color_);
        Point position = new Point(position_);
        position.x -= offset.x;
        position.y -= offset.y;
        Point2D center = new Point2D.Float(position.x, position.y);
        float[] dist = {0.25f, .75f};
        Color[] colors = {Color.WHITE, Color.red.darker()};
        RadialGradientPaint paint = new RadialGradientPaint(center, radius_.floatValue(), dist,
                colors, MultipleGradientPaint.CycleMethod.REFLECT);
        g2d.setPaint(paint);
        Shape theCircle;
        theCircle = new Ellipse2D.Double(position.x - radius_,
                position.y - radius_, 2.0 * radius_, 2.0 * radius_);
        g2d.fill(theCircle);
    }

    @Override
    public void Tick() {
        radius_ -= 9;
        if (radius_ <= 0) {
            is_dead_ = true;
            radius_ = 1.;
        }
    }

}
