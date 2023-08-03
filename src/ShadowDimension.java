import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 * Please enter your name below
 * Daniel Nappa
 */

public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int REFRESH_RATE = 60;
    private final static int WAIT_SECONDS = 3;
    private final static float TITLE_R = 104;
    private final static float TITLE_G = 153;
    private final static float TITLE_B = 153;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
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
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String INSTRUCTION_A = "PRESS SPACE TO START";
    private final static String INSTRUCTION_B = "USE ARROW KEYS TO FIND GATE";
    private final static String LEVEL_COMPLETE = "LEVEL COMPLETE!";
    private final static String ATTACK_INSTRUCTION = "PRESS A TO ATTACK";
    private final static String DEFEAT_NAVEC = "DEFEAT NAVEC TO WIN";
    private final static String WIN = "CONGRATULATIONS!";
    private final static String LOSE = "GAME OVER!";
    private final static int MAX_TIMESCALE = 3;
    private final static int MIN_TIMESCALE = -3;
    private final static String WALL_PATH = "res/wall.png";
    private final static String TREE_PATH = "res/tree.png";
    private int currLevel = 0;
    private final static int LEVEL_0 =  0;
    private final static int LEVEL_1 = 1;
    private final static int INCREASE = 0;
    private final static int DECREASE = 1;
    private final static int POWER = 2;
    private final static String PERCENT = "%";
    private final static String COMMA = ",";
    private final static int TYPE = 0;
    private final static int X = 1;
    private final static int Y = 2;
    private final static int GATE_X = 950;
    private final static int GATE_Y = 670;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int INSTRUCTION_X = TITLE_X + 90;
    private final static int INSTRUCTION_A_Y = TITLE_Y + 190;
    private final static int INSTRUCTION_B_Y = TITLE_Y + 230;
    private final static int INSTRUCTION_A_X_L1 = 350;
    private final static int INSTRUCTION_A_Y_L1 = 350;
    private final static int ATTACK_INSTRUCTION_Y = 390;
    private final static int DEFEAT_NAVEC_Y = 430;
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int UP = 2;
    private final static int DOWN = 3;
    private final static int TOP_LEFT = 0;
    private final static int BOTTOM_LEFT = 1;
    private final static int TOP_RIGHT = 2;
    private final static int BOTTOM_RIGHT = 3;
    private int currTimeScale = 0;
    private final static int STEP_SIZE = 2;
    private final static int defaultSize = 75;
    private final static int healthSize = 30;
    private final static int titleSize = 40;
    private final Font defaultFont = new Font("res/frostbite.ttf", defaultSize);
    private final Font healthFont = new Font("res/frostbite.ttf", healthSize);
    private final DrawOptions healthRGB = new DrawOptions();
    private final Font titleMessage = new Font("res/frostbite.ttf", titleSize);
    private Level level0;
    private Level level1;
    private boolean gameStarted = false;
    private boolean waiting = false;
    private boolean loadedNext = false;
    private int currDirection;
    private int frameCounter = 0;
    private ArrayList<Sprite> walls = new ArrayList<>();
    private ArrayList<Sprite> level0sinkHoles = new ArrayList<>();
    private ArrayList<Sprite> level1sinkHoles = new ArrayList<>();
    private ArrayList<Sprite> trees = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Enemy navec;
    private Player player0;
    private Point currPos;

    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        currDirection = RIGHT;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.readCSV("res/level0.csv");
        game.run();
    }

    /**
     * Method used to read file and create objects (You can change this
     * method as you wish).
     */
    private void readCSV(String fileName) {
        /* adopted from Lecture Slides (inputOutput-2.pdf) */
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                /* tokenize the csv line into array of strings */
                String[] fields = line.split(COMMA);
                Point pos = new Point(Double.parseDouble(fields[X]), Double.parseDouble(fields[Y]));
                /* create instance of appropriate sprite according to field and add it to objects array list */
                switch (fields[TYPE]) {
                    case "Fae":
                        currPos = new Point(Double.parseDouble(fields[X]), Double.parseDouble(fields[Y]));
                        player0 = new Player(currPos);
                        break;
                    case "Wall":
                        walls.add(new Obstacle(pos, WALL_PATH));
                        break;
                    case "Sinkhole":
                        switch (fileName) {
                            case "res/level0.csv":
                                level0sinkHoles.add(new SinkHole(pos));
                                break;
                            case "res/level1.csv":
                                level1sinkHoles.add(new SinkHole(pos));
                                break;
                        }
                        break;
                    case "Tree":
                        trees.add(new Obstacle(pos, TREE_PATH));
                        break;
                    case "Demon":
                        enemies.add(new Enemy(new Point(Double.parseDouble(fields[X]),
                                                       Double.parseDouble(fields[Y])), false));
                        break;
                    case "Navec":
                        navec = new Enemy(new Point(Double.parseDouble(fields[X]),
                                Double.parseDouble(fields[Y])), true);
                        enemies.add(navec);

                        break;
                    case "TopLeft":
                        switch (fileName) {
                            case "res/level0.csv":
                                level0 = new Level("res/background0.png");
                                level0.setLevelBoundaryTopLeft(new Point(Double.parseDouble(fields[X]),
                                        Double.parseDouble(fields[Y])));
                                break;
                            case "res/level1.csv":
                                level1 = new Level("res/background1.png");
                                level1.setLevelBoundaryTopLeft(new Point(Double.parseDouble(fields[X]),
                                        Double.parseDouble(fields[Y])));
                                break;
                        }
                            break;
                            case "BottomRight":
                                switch (fileName) {
                                    case "res/level0.csv":
                                        level0.setLevelBoundary(new Rectangle(level0.getLevelBoundaryTopLeft(),
                                                Double.parseDouble(fields[X]), Double.parseDouble(fields[Y])));
                                        break;
                                    case "res/level1.csv":
                                        level1.setLevelBoundary(new Rectangle(level1.getLevelBoundaryTopLeft(),
                                                Double.parseDouble(fields[X]), Double.parseDouble(fields[Y])));
                                        break;
                                }
                                break;
                        }

                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

    }
    /**
     * Returns true if player or enemy moves outside entire level boundary
     */
    private boolean outOfBounds(Point pos, Rectangle rect) {
        return (pos.x <= rect.left()) || (pos.x >= rect.right())
                || (pos.y <= rect.top()) || (pos.y >= rect.bottom());

    }
    /**
     * Draws message onto screen and centres it according to the dimensions of the text
     */
    private void drawMessageCentered(String message) {
        defaultFont.drawString(message, (Window.getWidth() / 2) - (defaultFont.getWidth(message) / 2),
                (Window.getHeight() / 2));
    }
    /**
     * Draws title screen for level 0 and beginning of level 1
     */
    private void drawMenu(boolean isNextLevel) {
        if (!isNextLevel) {
            defaultFont.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            titleMessage.drawString(INSTRUCTION_A, INSTRUCTION_X, INSTRUCTION_A_Y);
            titleMessage.drawString(INSTRUCTION_B, INSTRUCTION_X, INSTRUCTION_B_Y);
        } else {
            titleMessage.drawString(INSTRUCTION_A, INSTRUCTION_A_X_L1, INSTRUCTION_A_Y_L1);
            titleMessage.drawString(ATTACK_INSTRUCTION, INSTRUCTION_A_X_L1, ATTACK_INSTRUCTION_Y);
            titleMessage.drawString(DEFEAT_NAVEC, INSTRUCTION_A_X_L1, DEFEAT_NAVEC_Y);

        }
    }
    /**
     * Draws player's current health onto the screen
     */
    private void drawHealthBar() {
        if (player0.getHealth() < ORANGE) {
            healthFont.drawString(Integer.toString(player0.getHealth()) + PERCENT,
                    HEALTH_X, HEALTH_Y, healthRGB.setBlendColour(ORANGE_R, ORANGE_G, ORANGE_B));
            if (player0.getHealth() < RED) {
                healthFont.drawString(Integer.toString(player0.getHealth()) + PERCENT,
                        HEALTH_X, HEALTH_Y, healthRGB.setBlendColour(RED_R, RED_G, RED_B));
            }
        } else {
            healthFont.drawString(Integer.toString(player0.getHealth()) + PERCENT,
                    HEALTH_X, HEALTH_Y, healthRGB.setBlendColour(GREEN_R, GREEN_G, GREEN_B));
        }
    }
    /**
     * Draws sprites of a given type onto the screen
     */
    private void drawSprites(ArrayList<Sprite> sprites) {
        for (Sprite s : sprites) {
            s.draw(s.getDirection(), s.getPos());
        }
    }

    /**
     * Checks if player or enemy intersects with a wall (level 0) or tree (level 1)
     */
    private boolean wallTreeCollision(Sprite character, ArrayList<Sprite> sprites) {
        for (Sprite s : sprites) {
            if (character.getCollBox().intersects(s.getCollBox())) {
                return true;
            }
        }
        return false;
    }

    private double distanceBetweenPlayerEnemy(Enemy e) {
        return Math.sqrt(Math.pow((player0.getCollBox().centre().y - e.getCollBox().centre().y), POWER)
                       + Math.pow((player0.getCollBox().centre().x - e.getCollBox().centre().x), POWER));
    }
    private void updateEnemies() {
        ArrayList<Sprite> collidables = trees;
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>(); /* for enemies that have died */
        for (Enemy e: enemies) {
            /* Aggressive demon or navec*/
            if (e.getIsAggressive()) {
                Point tmpPos; /* prospective next position */
                Point prevPos = e.getPos();
                if (e.getDirection() == UP) {
                    tmpPos = new Point(e.getPos().x, e.getPos().y - e.getMovementSpeed());
                    e.updateCollBox(e.getEnemyLeft(), tmpPos);

                } else if (e.getDirection() == DOWN) {
                    tmpPos = new Point(e.getPos().x, e.getPos().y + e.getMovementSpeed());
                    e.updateCollBox(e.getEnemyLeft(), tmpPos);

                } else if (e.getDirection() == LEFT) {
                    tmpPos = new Point(e.getPos().x - e.getMovementSpeed(), e.getPos().y);
                    e.updateCollBox(e.getEnemyLeft(), tmpPos);
                    e.setDirection(LEFT);

                } else {
                    tmpPos = new Point(e.getPos().x + e.getMovementSpeed(), e.getPos().y);
                    e.updateCollBox(e.getEnemyLeft(), tmpPos);
                    e.setDirection(RIGHT);
                }

                /* collision with tree or out of bounds */
                if (wallTreeCollision(e, collidables) || outOfBounds(tmpPos, level1.getLevelBoundary())) {
                    /* go back to previous position and go opposite direction */
                    e.updateCollBox(e.getEnemyLeft(), prevPos);
                    e.changeDirection();
                /* update position if all checks pass */
                } else {
                    e.setPos(tmpPos);
                }
                /* collision with any sinkhole then go opposite direction */
                for (Sprite s : level1sinkHoles) {
                    if (e.getCollBox().intersects(s.getCollBox())) {
                        e.changeDirection();
                    }
                }
            }
            if (e.getIsInvincible()) {
                if (e.getFrameCounter() <= REFRESH_RATE * e.getInvincibleDuration()) {
                    e.incrementFrameCounter();
                } else {
                    e.changeInvincibleState();
                    e.resetFrameCounter();
                }
            }
            /* player is nearby draw fire */
            if (distanceBetweenPlayerEnemy(e) < e.getAttackRange()) {
                e.changeAttackState();

                if (player0.getCollBox().centre().x <= e.getCollBox().centre().x
                        && player0.getCollBox().centre().y <= e.getCollBox().centre().y) {
                    e.drawFire(TOP_LEFT);

                } else if (player0.getCollBox().centre().x <= e.getCollBox().centre().x
                        && player0.getCollBox().centre().y > e.getCollBox().centre().y) {
                    e.drawFire(BOTTOM_LEFT);

                } else if (player0.getCollBox().centre().x > e.getCollBox().centre().x
                        && player0.getCollBox().centre().y <= e.getCollBox().centre().y) {
                    e.drawFire(TOP_RIGHT);

                } else if (player0.getCollBox().centre().x > e.getCollBox().centre().x
                        && player0.getCollBox().centre().y > e.getCollBox().centre().y) {
                    e.drawFire(BOTTOM_RIGHT);
                }

                /* player intersects enemy fire attack */
                if (e.getEnemyFireCollBox().intersects(player0.getCollBox())) {
                    if (!player0.getIsInvincible()) {
                        player0.attackedByEnemy(e.getAttackDamage(), e.getIsNavec());
                    }
                }
            }
            e.draw(e.getDirection(), e.getPos());
            if (e.getHealth() == e.getMinHealth()) {
                enemiesToRemove.add(e);
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    /**
     * Update the timescale of the enemies
     */
    private void changeDifficulty(int decision) {
        for (Enemy e: enemies) {
            if (decision == INCREASE) {
                e.changeTimeScale(e.getTimescaleIncrease());
            } else if (decision == DECREASE) {
                e.changeTimeScale(e.getTimescaleDecrease());
            }
            currTimeScale = e.getTimeScale();
        }
    }
    private void movePlayer(Level level, int direction, ArrayList<Sprite> sinkHoles, int currLevel) {
        Point tmpPos; /* prospective next position */
        ArrayList<Sprite> collidables = walls;

        if (direction == UP) {
            tmpPos = new Point(currPos.x, currPos.y - STEP_SIZE);
            player0.updateCollBox(tmpPos);

        } else if (direction == DOWN) {
            tmpPos = new Point(currPos.x, currPos.y + STEP_SIZE);
            player0.updateCollBox(tmpPos);

        } else if (direction == LEFT) {
            tmpPos = new Point(currPos.x - STEP_SIZE, currPos.y);
            player0.updateCollBox(tmpPos);
            currDirection = LEFT;

        } else {
            tmpPos = new Point(currPos.x + STEP_SIZE, currPos.y);
            player0.updateCollBox(tmpPos);
            currDirection = RIGHT;
        }

        if (currLevel == LEVEL_1) {
            collidables = trees;
        }

        /* collision with wall (level 0) or tree (level 1) or out of bounds */
        if (wallTreeCollision(player0, collidables) || outOfBounds(tmpPos, level.getLevelBoundary())) {
            /* go back to previous position */
            player0.updateCollBox(currPos);
        } else {
            currPos = tmpPos;
        }
        // need to create auxiliary array list since removals cannot be performed when iterating over original
        // array
        ArrayList<Sprite> sinkHolesToRemove = new ArrayList<>();

        for (Sprite s : sinkHoles) {
            if (player0.getCollBox().intersects(s.getCollBox())){
                sinkHolesToRemove.add(s);
                player0.sinkHoleCollision();
            }
        }
        sinkHoles.removeAll(sinkHolesToRemove);

    }

    /**
     * Draws in-game objects and computes logic for the player
     * @param sinkHoles
     */
    private void updateInGameState(ArrayList<Sprite> sinkHoles) {
        // Draw each game object from csv file
        if (currLevel == LEVEL_0) {
            drawSprites(walls);
        } else {
            drawSprites(trees);
            updateEnemies();
        }
        drawSprites(sinkHoles);

        /* Player in attack state */
        if (player0.getIsAttack()) {
            if (player0.getPlayerFrameCounter() <= REFRESH_RATE) {
                /* Check if player is attacking any enemies */
                for (Enemy e: enemies) {
                    if (player0.getCollBox().intersects(e.getCollBox())) {
                        /* enemy is not in invincible state so attack is performed */
                        if (!e.getIsInvincible()) {
                            e.attackedByPlayer();
                            e.changeInvincibleState();
                        }
                    }
                }
                player0.incrementFrameCounter();
            } else {
                player0.changeAttackState();
                player0.changeCooldownState();
                player0.resetFrameCounter();
            }
        }

        /* Player in cooldown state after attack */
        player0.checkState(true);

        /* Player in invincible state after receiving attack */
        player0.checkState(false);
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        Level level = level0;
        ArrayList<Sprite> sinkHoles = level0sinkHoles;

        /* Main Menu */
        if (!gameStarted) {
            Window.setClearColour(TITLE_R, TITLE_G, TITLE_B);
            drawMenu(false);

            if (input.wasPressed(Keys.SPACE)) {
                gameStarted = true;
            }
        }

        if (currLevel == LEVEL_1) {
            /* load next level */
            if (!loadedNext) {
                readCSV("res/level1.csv");
                loadedNext = true;
            }
            level = level1;
            sinkHoles = level1sinkHoles;
        }
        /* Check if we have reached next level and if it's less than three seconds after doing so
        then wait, otherwise render the next start screen */
        if (waiting) {
            if (frameCounter <= REFRESH_RATE * WAIT_SECONDS) {
                drawMessageCentered(LEVEL_COMPLETE);
                frameCounter++;
            } else {
                drawMenu(true);
                if (input.wasPressed(Keys.SPACE)) {
                    currLevel = LEVEL_1;
                    waiting = false;
                }
            }
        }
        /* Game Over */
        else if (player0.getHealth() == player0.getMinHealth()) {
            drawMessageCentered(LOSE);
        }
        /* Level 0 Passed */
        else if (currLevel == LEVEL_0 && currPos.x >= GATE_X && currPos.y >= GATE_Y) {
            waiting = true;
            walls.clear();
        }
        /* Game won*/
        else if (currLevel == LEVEL_1 && navec.getHealth() == navec.getMinHealth()) {
            drawMessageCentered(WIN);
        }
        /* In-Game */
        else if (!waiting && gameStarted) {
            level.draw();
            updateInGameState(sinkHoles);

            if (input.wasPressed(Keys.A) && !player0.getIsAttack() &! player0.getCooldown()) {
                player0.changeAttackState();
            }

            if (input.wasPressed(Keys.L) && currTimeScale < MAX_TIMESCALE) {
                changeDifficulty(INCREASE);
                System.out.println("Sped up, Speed:\t" + currTimeScale);
            }

            if (input.wasPressed(Keys.K) && currTimeScale > MIN_TIMESCALE) {
                changeDifficulty(DECREASE);
                System.out.println("Slowed down, Speed:\t" + currTimeScale);
            }

            if (input.isDown(Keys.UP)) {
                movePlayer(level, UP, sinkHoles, currLevel);
            }

            if (input.isDown(Keys.DOWN)) {
                movePlayer(level, DOWN, sinkHoles, currLevel);
            }

            if (input.isDown(Keys.LEFT)) {
                movePlayer(level, LEFT, sinkHoles, currLevel);
            }

            if (input.isDown(Keys.RIGHT)) {
                movePlayer(level, RIGHT, sinkHoles, currLevel);
            }
            drawHealthBar();
            player0.draw(currDirection, currPos);
        }
        /* --DEBUGGING PURPOSES-- */
        /* skip straight to level 1 */
        if (input.wasPressed(Keys.W)){
            currLevel = LEVEL_1;
        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }
}
