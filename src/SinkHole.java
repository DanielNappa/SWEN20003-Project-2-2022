import bagel.Image;
import bagel.util.Point;

public class SinkHole extends Sprite {
    private final Image sinkHole = new Image("res/sinkhole.png");

    public SinkHole(Point pos) {
        super.setPos(new Point(pos.x, pos.y));
        /* need to shift collision rectangle coordinate since it's centered and not drawn from the top left */
        super.setCollBoxPos(new Point(pos.x, pos.y));
        super.setCollBox(sinkHole.getBoundingBoxAt(super.getCollBoxPos()));
        super.getCollBox().moveTo(super.getCollBoxPos());

    }
    @Override
    public void draw(int direction, Point pos) {
        sinkHole.drawFromTopLeft(pos.x, pos.y);

    }
}
