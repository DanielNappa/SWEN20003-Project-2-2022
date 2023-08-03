import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Level {
    private Image backgroundImage;
    private Rectangle levelBoundary;
    private Point levelBoundaryTopLeft;

    public Level(String imagePath) {
        backgroundImage = new Image(imagePath);
    }

    public Rectangle getLevelBoundary() {
        return levelBoundary;
    }

    public void setLevelBoundary(Rectangle levelBoundary) {
        this.levelBoundary = levelBoundary;
    }

    public Point getLevelBoundaryTopLeft() {
        return levelBoundaryTopLeft;
    }

    public void setLevelBoundaryTopLeft(Point levelBoundaryTopLeft) {
        this.levelBoundaryTopLeft = levelBoundaryTopLeft;
    }

    public void draw() {
        backgroundImage.drawFromTopLeft(backgroundImage.getBoundingBox().topLeft().x,
                                        backgroundImage.getBoundingBox().topLeft().y);


    }
}
