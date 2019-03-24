package model;

import javafx.scene.image.Image;

public final class Sprites {

    private static String dirHeroB = "../view/resources/images/heroB/";
    private static String dirHeroG = "../view/resources/images/heroG/";
    private static String dirMage = "../view/resources/images/mage/";
    private static String dirHiker = "../view/resources/images/hiker/";
    private static String dirSprinter = "../view/resources/images/sprinter/";
    private static String dirSpy = "../view/resources/images/spy/";
    private static String dirEnemy = "../view/resources/images/knight/";

    // Sprites used for player
    private static final Image[] heroBLeft = directionSprite(dirHeroB + "left");
    private static final Image[] heroBUp = directionSprite(dirHeroB + "up");
    private static final Image[] heroBRight = directionSprite(dirHeroB + "right");
    private static final Image[] heroBDown = directionSprite(dirHeroB + "down");
    private static final Image[] heroBDying = dyingSprite(dirHeroB + "dying");

    private static final Image[] heroGLeft = directionSprite(dirHeroG + "left");
    private static final Image[] heroGUp = directionSprite(dirHeroG + "up");
    private static final Image[] heroGRight = directionSprite(dirHeroG + "right");
    private static final Image[] heroGDown = directionSprite(dirHeroG + "down");
    private static final Image[] heroGDying = dyingSprite(dirHeroG + "dying");

    private static final Image[] mageLeft = directionSprite(dirMage + "left");
    private static final Image[] mageUp = directionSprite(dirMage + "up");
    private static final Image[] mageRight = directionSprite(dirMage + "right");
    private static final Image[] mageDown = directionSprite(dirMage + "down");
    private static final Image[] mageDying = dyingSprite(dirMage + "dying");

    private static final Image[] hikerLeft = directionSprite(dirHiker + "left");
    private static final Image[] hikerUp = directionSprite(dirHiker + "up");
    private static final Image[] hikerRight = directionSprite(dirHiker + "right");
    private static final Image[] hikerDown = directionSprite(dirHiker + "down");
    private static final Image[] hikerDying = dyingSprite(dirHiker + "dying");

    private static final Image[] sprinterLeft = directionSprite(dirSprinter + "left");
    private static final Image[] sprinterUp = directionSprite(dirSprinter + "up");
    private static final Image[] sprinterRight = directionSprite(dirSprinter + "right");
    private static final Image[] sprinterDown = directionSprite(dirSprinter + "down");
    private static final Image[] sprinterDying = dyingSprite(dirSprinter + "dying");

    private static final Image[] spyLeft = directionSprite(dirSpy + "left");
    private static final Image[] spyUp = directionSprite(dirSpy + "up");
    private static final Image[] spyRight = directionSprite(dirSpy + "right");
    private static final Image[] spyDown = directionSprite(dirSpy + "down");
    private static final Image[] spyDying = dyingSprite(dirSpy + "dying");

    private static final Image[] enemyLeft = directionSprite(dirEnemy + "left");
    private static final Image[] enemyUp = directionSprite(dirEnemy + "up");
    private static final Image[] enemyRight = directionSprite(dirEnemy + "right");
    private static final Image[] enemyDown = directionSprite(dirEnemy + "down");




    private static Image[] directionSprite(String dirDirection) {
        Image[] movementSprites = new Image[9];
        for (int i = 0; i < 9; i ++) {
            movementSprites[i] = new Image(Sprites.class.getResourceAsStream(dirDirection + (i+1) + ".png"));
        }
        return movementSprites;
    }
    private static Image[] dyingSprite(String dirDirection) {
        Image[] dyingSprites = new Image[6];
        for (int i = 0; i < 6; i ++) {
            dyingSprites[i] = new Image(Sprites.class.getResourceAsStream(dirDirection + (i+1) + ".png"));
        }
        return dyingSprites;
    }

    public static final Image[][] heroBSprites = new Image[][] {heroBLeft, heroBUp, heroBRight, heroBDown, heroBDying};
    public static final Image[][] heroGSprites = new Image[][] {heroGLeft, heroGUp, heroGRight, heroGDown, heroGDying};
    public static final Image[][] mageSprites = new Image[][] {mageLeft, mageUp, mageRight, mageDown, mageDying};
    public static final Image[][] hikerSprites = new Image[][] {hikerLeft, hikerUp, hikerRight, hikerDown, hikerDying};
    public static final Image[][] sprinterSprites = new Image[][] {sprinterLeft, sprinterUp, sprinterRight, sprinterDown, sprinterDying};
    public static final Image[][] spySprites = new Image[][] {spyLeft, spyUp, spyRight, spyDown, spyDying};

    public static final Image[][] enemySprites = new Image[][] {enemyLeft, enemyUp, enemyRight, enemyDown};

}