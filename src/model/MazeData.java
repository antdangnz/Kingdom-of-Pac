package model;

import java.util.ArrayList;
import java.util.List;

public final class MazeData {

    static final int EMPTY_SPACE = 0;
    static final int BLOCK = 1;
    static final int PELLET = 2;
    static final int POWER_UP = 3;
    static final int HERO_START = 4;
    static final int ENEMY_START = 5;

    private static int playerStartX, playerStartY;
    private static List<Integer> enemyStartingX = new ArrayList<>(), enemyStartingY = new ArrayList<>();


    static final int MAX_BLOCKS_H = 32, MAX_BLOCKS_V = 30;
    private static final List<List<Integer>> MAZE_MAP = new ArrayList<>();
    private static final Object[][] DOT_POINTERS = new Object[MAX_BLOCKS_H + 1][MAX_BLOCKS_V + 1];

    private static int totalPellets = 0;

    public static int getPlayerStartingX() {
        return playerStartX;
    }

    public static int getPlayerStartingY() {
        return playerStartY;
    }

    public static int getEnemyPositionX(int index) {
        return enemyStartingX.get(index);
    }

    public static int getEnemyPositionY(int index) {
        return enemyStartingY.get(index);
    }

    public static int getMazePositionType(int x, int y) {
        return MAZE_MAP.get(y).get(x);
    }

    public static Object getPellet(int x, int y) {
        return DOT_POINTERS[x][y];
    }

    public static int getTotalPellets() {
        return totalPellets;
    }

    public static void setPlayerStartingPoint(int x, int y) {
        playerStartX = x;
        playerStartY = y;
    }

    public static void setEnemyStartingX(int x) {
        enemyStartingX.add(x);
    }

    public static void setEnemyStartingY(int y) {
        enemyStartingY.add(y);
    }

    public static void resetMazeMap() {
        MAZE_MAP.clear();
    }

    public static void setMazeMapRows(List<Integer> line) {
        MAZE_MAP.add(line);
    }

    public static void setPellet(int x, int y, Object pellet) {
        DOT_POINTERS[x][y] = pellet;
    }

    public static void increasePelletCount() {
        totalPellets++;
    }

    public static void resetTotalPellets() {
        totalPellets = 0;
    }

    public static void resetEmptyEnemyStarts() {
        enemyStartingX.clear();
        enemyStartingY.clear();
    }

    public static boolean canMoveHere(int x, int y) {
        return MazeData.getMazePositionType(x, y) != MazeData.BLOCK;
    }

    public static boolean outOfBoundaries(int x, int y) {
        return x < 1 || y < 1
                || x >= MazeData.MAX_BLOCKS_H
                || y >= MazeData.MAX_BLOCKS_V;
    }

    public static boolean canWrapLower(int x, int y) {
        return (x <= 0)
                && (MazeData.getMazePositionType(MazeData.MAX_BLOCKS_H - 1, y) != MazeData.BLOCK)
                || (y <= 0)
                && (MazeData.getMazePositionType(x, MazeData.MAX_BLOCKS_V - 1) != MazeData.BLOCK);
    }

    public static boolean canWrapUpper(int x, int y) {
        return (x >= MazeData.MAX_BLOCKS_H - 1)
                && (MazeData.getMazePositionType(0, y) != MazeData.BLOCK)
                || (y >= MazeData.MAX_BLOCKS_V - 1)
                && (MazeData.getMazePositionType(x, 0) != MazeData.BLOCK);
    }
}
