import bagel.Image;
import bagel.util.Point;

public class Obstacle extends Sprite {
    private Image obstacle;

    public Obstacle(Point pos, String fileName) {
        super.setPos(new Point(pos.x, pos.y));
        /* need to shift collision rectangle coordinate since it's centered and not drawn from the top left */
        obstacle = new Image(fileName);
        super.setCollBoxPos(new Point(pos.x, pos.y));
        super.setCollBox(obstacle.getBoundingBoxAt(super.getCollBoxPos()));
        super.getCollBox().moveTo(super.getCollBoxPos());

    }
    @Override
    public void draw(int direction, Point pos) {
        obstacle.drawFromTopLeft(pos.x, pos.y);

    }
}
