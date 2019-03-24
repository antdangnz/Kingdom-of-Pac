package model;

public class DirectionChoice {

    private int x, y, heroX, heroY, score;
    private boolean runAway;

    public DirectionChoice(int x, int y, int heroX, int heroY, boolean runAway) {
        this.x = x;
        this.y = y;
        this.heroX = heroX;
        this.heroY = heroY;
        this.score = 500;
        this.runAway = runAway;
        determineDirection();
    }

    private void determineDirection() {
        if (MazeData.outOfBoundaries(this.x, this.y)) {
            score = -1;
            return;
        }

        if (!MazeData.canMoveHere(this.x, this.y)) {
            this.score = -1;
            return;
        }

        int distance = Math.abs(this.x - heroX) + Math.abs(this.y - heroY);

        if (runAway) {
        this.score += distance;
        } else {
        this.score -= distance;

        }

    }

    public int getScore() {
        return this.score;
    }

    public boolean isScoreGreater(int otherScore) {
        return this.score > otherScore;
    }



}
