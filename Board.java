package genetic;

import java.util.ArrayList;

public class Board {
    private final int sizeX;
    private final int sizeY;
    private ArrayList<Path> paths = new ArrayList<>();
    private int fitness = Integer.MAX_VALUE;

    public Board(int[] size, int[] pairCoordinates) throws IllegalArgumentException {
        if (!checkValues(size, pairCoordinates)) {
            throw new IllegalArgumentException("Size field should contain two positive numbers; coordinates of the points are to be presented as the array of elements of the form X1, Y1, X2, Y2");
        }
        sizeX = size[0];
        sizeY = size[1];
        //creating pairs
        int currX1 = -1;
        int currX2 = -1;
        int currY1 = -1;
        int currY2 = -1;
        for (int i = 0; i < pairCoordinates.length; i++) {
            if (i % 4 == 0) {
                currX1 = pairCoordinates[i];
            }
            if (i % 4 == 1) {
                currY1 = pairCoordinates[i];
            }
            if (i % 4 == 2) {
                currX2 = pairCoordinates[i];
            }
            if (i % 4 == 3) {
                currY2 = pairCoordinates[i];
                paths.add(new Path(currX1, currY1, currX2, currY2, this.sizeX, this.sizeY));
            }

        }
    }

    public Board(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    //checking the values of the inputs
    public boolean checkValues(int[] size, int[] pairs) {
        if (size.length != 2 || pairs.length < 4 || pairs.length % 4 != 0) {
            return false;
        }
        if (size[0] <= 0 || size[1] <= 0) {
            return false;
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] <= 0) return false;
        }
        String[] coordinates = new String[pairs.length / 2];
        for (int i = 0; i < pairs.length; i++) {
            if (i % 2 == 1) {
                coordinates[i / 2] = "(" + pairs[i - 1] + "; " + pairs[i] + ")";
            }
        }
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = i + 1; j < coordinates.length; j++) {
                if (coordinates[i].equals(coordinates[j])) return false;
            }
        }
        return true;
    }

    public void generateAllPath() {
        for (Path p : paths) {
            p.generatePath();
        }
    }

    @Override
    public String toString() {
        String s = "Size: (" + sizeX + ", " + sizeY + ")\n";
        for (int i = 0; i < paths.size(); i++) {
            s += "Pair number: " + i + "\n";
            s += paths.get(i) + "\n";
        }
        return s;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public ArrayList<Path> getPairs() {
        //todo it might return the copy
        return paths;
    }

    public int calculateFitness() {
        int fitness = 0;
        //needed because numbers of segments is less than number of actual vires by 1
        int additionalConnector = 1;
        int numOfCellsWithMoreThanOneElements = 0;
        int pathLength = 0;
        int totalPath = 0;
        int[][] field = new int[sizeY][sizeX];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = 0;
            }
        }
        for (Path path : this.paths) {
            field[path.getStarting().getY() - 1][path.getStarting().getX() - 1] += 1;
            field[path.getEnding().getY() - 1][path.getEnding().getX() - 1] += 1;
            ArrayList<Segment> seg = path.getSegments();
            pathLength = seg.size() + additionalConnector;
            totalPath+=pathLength;
            for (int j = 0; j < seg.size(); j++) {
                field[seg.get(j).getY() - 1][seg.get(j).getX() - 1] += 1;
            }
        }
        for (int i = field.length - 1; i >= 0; i--) {
            for (int j = 0; j < field[i].length; j++) {
                //System.out.print(field[i][j]+"  ");
                if (field[i][j] > 1) {
                    numOfCellsWithMoreThanOneElements += field[i][j] - 1;
                }
//                System.out.println();
            }
        }
        //todo literals should be variables
        fitness = numOfCellsWithMoreThanOneElements * 1000 + totalPath;
        this.fitness = fitness;
        return fitness;
    }

    public int getFitness() {
        return fitness;
    }

    public void addPairs(ArrayList<Path> p) {
        for (Path path : p) {

            //todo cloning
            paths.add(path.clone());
        }
    }

    public void setPairs(ArrayList<Path> paths) {
        this.paths = paths;
    }
    //todo
    @Override
    public Board clone(){
        Board newBoard = new Board(sizeX, sizeY);
        newBoard.setPairs(new ArrayList<Path>());
        ArrayList<Path> newPaths = new ArrayList<>();
        for(Path p: paths){
            newPaths.add(p.clone());
        }
        newBoard.addPairs(newPaths);
        return newBoard;
        //todo check
    }
}
