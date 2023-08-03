import java.lang.Math;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Enemy extends Sprite {
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int UP = 2;
    private final static int DOWN = 3;
    private final static int TOP_LEFT = 0;
    private final static int BOTTOM_LEFT = 1;
    private final static int TOP_RIGHT = 2;
    private final static int BOTTOM_RIGHT = 3;
    private final static double PI_2 = Math.PI / 2;
    private final static double THREE_PI_2 = 3 * PI_2;
    private final static int MIN_HEALTH = 0;
    private final static int DEMON_MAX_HEALTH = 40;
    private final static int SIZE = 15;
    private final static int HEALTH_Y = 6;
    private final static int FACTOR = 100;
    private final static int DEMON_ATTACK_RANGE = 150;
    private final static int DEMON_ATTACK_DAMAGE = 10;
    private final static int NAVEC_MAX_HEALTH = 80;
    private final static int NAVEC_ATTACK_RANGE = 200;
    private final static int NAVEC_ATTACK_DAMAGE = 20;
    private final static int PLAYER_ATTACK_DAMAGE = 20;
    private final static double RAND_MIN  = 0.2;
    private final static double RAND_MAX  = 0.7;
    private final static double TIMESCALE_INCREASE = 1.5;
    private final static double TIMESCALE_DECREASE = 0.5;
    private final static int MAX_TIMESCALE = 3;
    private final static int MIN_TIMESCALE = -3;
    private final static int INVINCIBLE_DURATION = 3;
    private final static double GREEN_R = 0;
    private final static double GREEN_B = 0.2;
    private final static double GREEN_G = 0.8;
    private final static int ORANGE = 65;
    private final static double ORANGE_R = 0.9;
    private final static double ORANGE_G = 0.6;
    private final static double ORANGE_B = 0;
    private final static int RED = 35;
    private final static double RED_R = 1;
    private final static double RED_B = 0;
    private final static double RED_G = 0;
    private final static String PERCENT = "%";
    private boolean isNavec = false;
    private boolean isAggressive;
    private boolean isInvincible = false;
    private boolean isAttack = false;
    private int health;
    private int maxHealth;
    private int attackRange;
    private int attackDamage;
    private double movementSpeed;
    private int timeScale;
    private int frameCounter = 0;

    private Image enemyLeft;
    private Image enemyRight;
    private Image enemyInvincibleLeft;
    private Image enemyInvincibleRight;
    private Image enemyFire;
    private Rectangle enemyFireCollBox;
    private final DrawOptions fireOrientation = new DrawOptions();
    private final Font healthFont = new Font("res/frostbite.ttf", SIZE);
    private final DrawOptions healthRGB = new DrawOptions();
    private static final String DEMON_LEFT = "res/demon/demonLeft.png";
    private static final String DEMON_RIGHT = "res/demon/demonRight.png";
    private static final String DEMON_INVINCIBLE_LEFT = "res/demon/demonInvincibleLeft.png";
    private static final String DEMON_INVINCIBLE_RIGHT = "res/demon/demonInvincibleRight.png";
    private static final String DEMON_FIRE = "res/demon/demonFire.png";
    private static final String NAVEC_LEFT = "res/navec/navecLeft.png";
    private static final String NAVEC_RIGHT = "res/navec/navecRight.png";
    private static final String NAVEC_INVINCIBLE_LEFT = "res/navec/navecInvincibleLeft.png";
    private static final String NAVEC_INVINCIBLE_RIGHT = "res/navec/navecInvincibleRight.png";
    private static final String NAVEC_FIRE = "res/navec/navecFire.png";

    public Enemy(Point pos, boolean isNavec) {
        super.setPos(new Point(pos.x, pos.y));
        Random rand = new Random();
        movementSpeed = RAND_MIN + (rand.nextDouble() * (RAND_MAX - RAND_MIN));
        super.setDirection(ThreadLocalRandom.current().nextInt(LEFT, DOWN + RIGHT));

        /* For demon */
        if (!isNavec) {
            enemyLeft = new Image(DEMON_LEFT);
            enemyRight = new Image(DEMON_RIGHT);
            enemyInvincibleLeft = new Image(DEMON_INVINCIBLE_LEFT);
            enemyInvincibleRight = new Image(DEMON_INVINCIBLE_RIGHT);
            enemyFire = new Image(DEMON_FIRE);
            isAggressive = rand.nextBoolean();
            health = DEMON_MAX_HEALTH;
            maxHealth = DEMON_MAX_HEALTH;
            attackRange = DEMON_ATTACK_RANGE;
            attackDamage = DEMON_ATTACK_DAMAGE;

        /* Navec */
        } else {
            enemyLeft = new Image(NAVEC_LEFT);
            enemyRight = new Image(NAVEC_RIGHT);
            enemyInvincibleLeft = new Image(NAVEC_INVINCIBLE_LEFT);
            enemyInvincibleRight = new Image(NAVEC_INVINCIBLE_RIGHT);
            enemyFire = new Image(NAVEC_FIRE);
            this.isNavec = true;
            isAggressive = true;
            health = NAVEC_MAX_HEALTH;
            maxHealth = NAVEC_MAX_HEALTH;
            attackRange = NAVEC_ATTACK_RANGE;
            attackDamage = NAVEC_ATTACK_DAMAGE;
        }
        timeScale = 0;
        updateCollBox(enemyLeft, super.getPos());
        updateFireCollBox(super.getPos());

    }

    public int getHealth() {
        return health;
    }
    public boolean getIsInvincible() { return isInvincible; }
    public Image getEnemyLeft() { return enemyLeft; }
    public Rectangle getEnemyFireCollBox() { return enemyFireCollBox; }
    public int getAttackRange() { return attackRange; }
    public int getAttackDamage() { return attackDamage; }
    public int getInvincibleDuration() { return INVINCIBLE_DURATION; }
    public double getMovementSpeed() { return movementSpeed; }
    public int getMinHealth() { return MIN_HEALTH; }
    public boolean getIsNavec() { return isNavec; }
    public boolean getIsAggressive() { return isAggressive; }

    public int getMaxTimescale() { return MAX_TIMESCALE; }
    public int getMinTimescale() { return MIN_TIMESCALE; }

    public double getTimescaleIncrease() { return TIMESCALE_INCREASE; }
    public double getTimescaleDecrease() { return TIMESCALE_DECREASE; }
    public int getFrameCounter() { return frameCounter; }
    public void incrementFrameCounter() { frameCounter++; }
    public void resetFrameCounter() { frameCounter = 0; }
    public int getTimeScale() { return timeScale; }

    /**
     * Changes the attack state of the enemy
     */
    /* Changes the attack state of the enemy */
    public void changeAttackState() {
        isAttack = !isAttack;
    }
    /**
     * Changes the invincible state of the enemy
     */
    public void changeInvincibleState() {
        isInvincible = !isInvincible;
    }

    /**
     * Attack received by player thus damage is inflicted
     */
    public void attackedByPlayer() {
        health -= PLAYER_ATTACK_DAMAGE;
        if (health < MIN_HEALTH) {
            health  = MIN_HEALTH;
        }
        /* attacks navec */
        if (isNavec) {
            System.out.println("Fae inflicts " + PLAYER_ATTACK_DAMAGE +
                    " damage points on Navec.\tNavec's current health:\t" +
                    health + "/" + maxHealth);
        } else {
            System.out.println("Fae inflicts " + PLAYER_ATTACK_DAMAGE +
                    " damage points on Demon.\tDemon's current health:\t" +
                    health + "/" + maxHealth);
        }
    }
    /**
     * Increases timescale of the enemy
     */
    /* Increases timescale of the enemies */
    public void changeTimeScale(double factor) {
        if (factor == TIMESCALE_INCREASE && timeScale < MAX_TIMESCALE) {
            timeScale++;
            movementSpeed *= factor;
        }
        else if (factor == TIMESCALE_DECREASE && MIN_TIMESCALE < timeScale) {
            timeScale--;
            movementSpeed *= factor;
        }
    }

    /**
     * Updates collision rectangle of enemy
     */
    public void updateCollBox(Image image, Point pos) {
        super.setCollBoxPos(new Point(pos.x, pos.y));
        super.setCollBox(image.getBoundingBoxAt(super.getCollBoxPos()));
        super.getCollBox().moveTo(super.getCollBoxPos());
    }

    /**
     * Updates the collision rectangle of flame
     */
    public void updateFireCollBox(Point pos) {
        enemyFireCollBox = enemyFire.getBoundingBoxAt(pos);
        enemyFireCollBox.moveTo(pos);
    }
    /**
     * Makes enemy travel in opposite direction
     */
    public void changeDirection() {
        if (super.getDirection() == LEFT) { super.setDirection(RIGHT); }
        else if (super.getDirection() == RIGHT) { super.setDirection(LEFT); }
        else if (super.getDirection() == UP) { super.setDirection(DOWN); }
        else if (super.getDirection() == DOWN) { super.setDirection(UP); }
    }

    /**
     * Displays the current enemy health
     */
    private void drawHealthBar() {
        double percent = ((double )health / maxHealth) * FACTOR;
        if ((int) percent * FACTOR < ORANGE) {
            healthFont.drawString(Integer.toString((int) percent) + PERCENT,
                    super.getPos().x, super.getPos().y - HEALTH_Y,
                    healthRGB.setBlendColour(ORANGE_R, ORANGE_G, ORANGE_B));
            if ((int) percent * FACTOR < RED) {
                healthFont.drawString(Integer.toString((int) percent) + PERCENT,
                        super.getPos().x, super.getPos().y - HEALTH_Y,
                        healthRGB.setBlendColour(RED_R, RED_G, RED_B));
            }
        } else {
            healthFont.drawString(Integer.toString((int) percent) + PERCENT,
                    super.getPos().x, super.getPos().y - HEALTH_Y,
                    healthRGB.setBlendColour(GREEN_R, GREEN_G, GREEN_B));
        }
    }

    /**
     * Draws fire around enemy in give orientation depending on position of the player
     */
    public void drawFire(int direction) {
        Point fireTopLeft;
        /* need to determine position where to draw the flame then create a collision box around
        * that position for the flame */
        if (direction == TOP_LEFT) {
            fireTopLeft = new Point(super.getCollBox().topLeft().x - enemyFire.getWidth(),
                    super.getCollBox().topLeft().y - enemyFire.getHeight());
            enemyFire.drawFromTopLeft(fireTopLeft.x, fireTopLeft.y);
            updateFireCollBox(fireTopLeft);
        } else if (direction == TOP_RIGHT) {
            fireTopLeft = new Point(super.getCollBox().topRight().x,
                    super.getCollBox().topRight().y - enemyFire.getHeight());
            enemyFire.drawFromTopLeft(fireTopLeft.x, fireTopLeft.y,
                    fireOrientation.setRotation(PI_2));
            updateFireCollBox(fireTopLeft);
        } else if (direction == BOTTOM_RIGHT) {
            fireTopLeft = new Point(super.getCollBox().bottomRight().x,
                    super.getCollBox().bottomRight().y);
            enemyFire.drawFromTopLeft(fireTopLeft.x, fireTopLeft.y,
                    fireOrientation.setRotation(Math.PI));
            updateFireCollBox(fireTopLeft);
        } else if (direction == BOTTOM_LEFT) {
            fireTopLeft = new Point(super.getCollBox().bottomLeft().x - enemyFire.getWidth(),
                    super.getCollBox().bottomLeft().y);
            enemyFire.drawFromTopLeft(fireTopLeft.x, fireTopLeft.y,
                    fireOrientation.setRotation(THREE_PI_2));
            updateFireCollBox(fireTopLeft);
        }

    }

    /**
     * draw the enemy on the screen
     */
    @Override
    public void draw(int direction, Point pos) {

        if (direction == RIGHT || direction == UP || direction == DOWN) {
            if (isInvincible) {
                enemyInvincibleRight.drawFromTopLeft(pos.x, pos.y);
            } else {
                enemyRight.drawFromTopLeft(pos.x, pos.y);
            }
        } else if (direction == LEFT) {
            if (isInvincible) {
                enemyInvincibleLeft.drawFromTopLeft(pos.x, pos.y);
            } else {
                enemyLeft.drawFromTopLeft(pos.x, pos.y);
            }
        }
        drawHealthBar();
    }
}
