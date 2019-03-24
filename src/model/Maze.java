package model;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    //String Version
    private List<String> mazeData;

    private Group gameZone;
    private File mazeFile;

    public Maze(Group gameZone) throws IOException, URISyntaxException {
        this.gameZone = gameZone;
        this.mazeData = new ArrayList<>();


        //Randomize level variable. Random map level between 1 and 3 ((random() * (max-min+1) + min).
        setMazeData((int)(Math.random() * 4) + 1);
        setMaze();
    }

    private void setMazeData(int i) {
        mazeFile = new File("../view/resources/mazes/maze" + i + ".txt");
    }

    private void setMaze() throws IOException, URISyntaxException {
//        mazeData = Files.readAllLines(Paths.get(mazeFilesList.get(level).getAbsolutePath()), StandardCharsets.UTF_8);
//        mazeData = Files.readAllLines(Paths.get(getClass().getResource(mazeFilesList.get(level).toString()).toURI()), StandardCharsets.UTF_8);
        mazeData = Files.readAllLines(Paths.get(getClass().getResource(mazeFile.toString()).toURI()), StandardCharsets.UTF_8);

//        Paths.get(mazeFilesList.get(level).toURI());
//        mazeData = Files.readAllLines(Paths.get(getClass().getResource("../view/resources/maze1.txt").toString()), StandardCharsets.UTF_8);
//        System.out.println(getClass().getResource("../view/resources/maze1.txt").toString());
//        System.out.println(maze.getAbsolutePath());
//        Paths.get(getClass().getResourceAsStream("").toString());

        convertFromString();
        setStartingPoints();
        createMazeElements();
    }

    private void convertFromString() {
        MazeData.resetMazeMap();
        for (int i = 0; i < MazeData.MAX_BLOCKS_V; i++) {
            List<Integer> nextLine = new ArrayList<>();
            for (int j = 0; j < MazeData.MAX_BLOCKS_H; j++) {
                nextLine.add(Character.getNumericValue(mazeData.get(i).charAt(j)));
            }
            MazeData.setMazeMapRows(nextLine);
        }
    }



    private void setStartingPoints() {
        MazeData.resetEmptyEnemyStarts();
        for (int i = 0; i < MazeData.MAX_BLOCKS_V; i++) { // y
            for (int j = 0; j < MazeData.MAX_BLOCKS_H; j++) { // x
                //Getting Player starting point
                if (MazeData.getMazePositionType(j, i) == MazeData.HERO_START) {
                    MazeData.setPlayerStartingPoint(j,i);
                }
                //Getting Ghost starting points
                else if (MazeData.getMazePositionType(j, i) == MazeData.ENEMY_START) {
                    MazeData.setEnemyStartingX(j);
                    MazeData.setEnemyStartingY(i);
                }
            }
        }
    }

    private void createMazeElements() {
        MazeData.resetTotalPellets();
        for (int i = 0; i < MazeData.MAX_BLOCKS_V; i++) {
            for (int j = 0; j < MazeData.MAX_BLOCKS_H; j++) {
                if (!MazeData.canMoveHere(j, i)) {
                    mazeWallGenerator(j, i);

                } else if (MazeData.getMazePositionType(j, i) == MazeData.PELLET) {

                    Pellet pellet = new Pellet(j, i, MazeData.PELLET);
                    MazeData.setPellet(j, i, pellet);
                    gameZone.getChildren().add(pellet.getCircle());
                    MazeData.increasePelletCount();

                } else if (MazeData.getMazePositionType(j, i) == MazeData.POWER_UP) {

                    Pellet pellet = new Pellet(j, i, MazeData.POWER_UP);
                    pellet.playTimeline();
                    MazeData.setPellet(j, i, pellet);
                    gameZone.getChildren().add(pellet.getCircle());
                    MazeData.increasePelletCount();
                }
            }
        }
    }

    private void mazeWallGenerator(int x, int y) {
        Rectangle rect = new Rectangle();
        rect.setWidth(32); //32
        rect.setHeight(24); //24
        rect.setStroke(Color.DARKGRAY);
        rect.setX(x * 32); //32
        rect.setY(y * 24); //24

        rect.setFill(Color.DARKGRAY);
        gameZone.getChildren().add(rect);

    }

    public void resetPellets() {
        for (int i = 0; i < MazeData.MAX_BLOCKS_V; i++) {
            for (int j = 0; j < MazeData.MAX_BLOCKS_H; j++) {
                if ((MazeData.getMazePositionType(j, i) == MazeData.PELLET) || (MazeData.getMazePositionType(j, i) == MazeData.POWER_UP)) {
                    Pellet pellet = (Pellet) MazeData.getPellet(j, i);
                    pellet.toggleVisibility();
                }
            }
        }
    }

}