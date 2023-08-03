import bagel.*;
import bagel.util.Point;

public class Player extends Sprite {
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int REFRESH_RATE = 60;
    private final Image PLAYER_LEFT = new Image("res/fae/faeLeft.png");

    private final Image PLAYER_RIGHT = new Image("res/fae/faeRight.png");
    private final Image PLAYER_LEFT_ATTACK = new Image("res/fae/faeAttackLeft.png");
    private final Image PLAYER_RIGHT_ATTACK = new Image("res/fae/faeAttackRight.png");
    private final static int SINKHOLE_DAMAGE = 30;
    private final static int ATTACK_COOLDOWN = 2;
    private final static int INVINCIBLE_DURATION = 3;
    private int playerFrameCounter = 0;
    private final static int MAX_HEALTH = 100;
    private final static int MIN_HEALTH = 0;

    private int health;
    private boolean isAttack = false;
    private boolean cooldown = false;
    private boolean isInvincible = false;

    public Player(Point pos) {
        super.setPos(new Point(pos.x, pos.y));
        /* need to shift collision rectangle coordinate since it's centered and not drawn from the top left */
        updateCollBox(super.getPos());
        health = MAX_HEALTH;

    }
    public void updateCollBox(Point pos) {
        super.setCollBoxPos(new Point(pos.x, pos.y));
        super.setCollBox(PLAYER_LEFT.getBoundingBoxAt(super.getCollBoxPos()));
        super.getCollBox().moveTo(super.getCollBoxPos());
    }

    public int getHealth() {
        return health;
    }

    public int getMinHealth() {
        return MIN_HEALTH;
    }
    public boolean getIsAttack() { return isAttack; }
    public boolean getCooldown() { return cooldown; }
    public boolean getIsInvincible() { return isInvincible; }
    public int getPlayerFrameCounter() { return playerFrameCounter; }
    public void incrementFrameCounter() { playerFrameCounter++; }
    public void resetFrameCounter() { playerFrameCounter = 0; }
    public void sinkHoleCollision() {
        health -= SINKHOLE_DAMAGE;
        if (health < MIN_HEALTH) {
            health  = MIN_HEALTH;
        }
        System.out.println("Sinkhole inflicts " + SINKHOLE_DAMAGE + " damage points on Fae.\tFae's current health:\t" +
                health + "/" + MAX_HEALTH);
    }
    /**
     * Player gets attacked by enemy
     */
    public void attackedByEnemy(int damageAmount, boolean isNavec) {
        health -= damageAmount;
        if (health < MIN_HEALTH) {
            health  = MIN_HEALTH;
        }
        if (isNavec) {
            System.out.println("Navec inflicts " + damageAmount + " damage points on Fae.\tFae's current health:\t" +
                    health + "/" + MAX_HEALTH);
        } else {
            System.out.println("Demon inflicts " + damageAmount + " damage points on Fae.\tFae's current health:\t" +
                    health + "/" + MAX_HEALTH);
        }
        changeInvincibleState();
    }

    /**
     * Changes the attack state of the player
     */
    public void changeAttackState() {
        isAttack = !isAttack;
    }

    /**
     * Changes the invincible state of the player
     */
    public void changeInvincibleState() {
        isInvincible = !isInvincible;
    }

    /**
     * Changes the cooldown state of the player
     */
    public void changeCooldownState() {
        cooldown = !cooldown;
    }

    /**
     * Checks and verifies the invincibility or cooldown status of the player
     */
    public void checkState(boolean checkIsCooldown) {
        /* Player in cooldown or invincible state after attack */
        if (checkIsCooldown) {
            if (cooldown) {
                if (playerFrameCounter <= REFRESH_RATE * ATTACK_COOLDOWN) {
                    incrementFrameCounter();
                } else {
                    changeCooldownState();
                    resetFrameCounter();
                }
            }
        } else {
            if (isInvincible) {
                if (playerFrameCounter <= REFRESH_RATE * INVINCIBLE_DURATION) {
                    incrementFrameCounter();
                } else {
                    changeInvincibleState();
                    resetFrameCounter();
                }
            }
        }
    }

    /**
     * Draw the player on the screen given the current direction
     */
    @Override
    public void draw(int direction, Point pos) {
        if (direction == LEFT) {
            if (isAttack) {
                PLAYER_LEFT_ATTACK.drawFromTopLeft(pos.x, pos.y);
            } else {
                PLAYER_LEFT.drawFromTopLeft(pos.x, pos.y);
            }
        }
        else if (direction == RIGHT) {
            if (isAttack) {
                PLAYER_RIGHT_ATTACK.drawFromTopLeft(pos.x, pos.y);
            } else {
                PLAYER_RIGHT.drawFromTopLeft(pos.x, pos.y);
            }
        }
    }
}
