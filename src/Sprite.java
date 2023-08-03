import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Sprite {
    /* position of sprite on screen */
    private Point pos;
    private Rectangle collBox;
    private Point collBoxPos;

    private int direction = 0;


    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getCollBoxPos() {
        return collBoxPos;
    }

    public void setCollBoxPos(Point collBoxPos) {
        this.collBoxPos = collBoxPos;
    }

    public Rectangle getCollBox() {
        return collBox;
    }

    public void setCollBox(Rectangle collBox) {
        this.collBox = collBox;
    }

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }



    /* draw the sprite on the screen */
    public abstract void draw(int direction, Point pos);


}
