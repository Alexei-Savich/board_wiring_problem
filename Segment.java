package genetic;

import java.lang.Math;
import java.util.Random;

public class Segment {
    private final int x;
    private final int y;
    private Object prev;
    private Object next;
    private final Path parent;
    boolean isCompleted = false;

    public Segment(int x, int y, Object prev, Path parent) {
        this.x = x;
        this.y = y;
        this.prev = prev;
        this.parent = parent;
    }

    //checking if the distance from this segment to the destination is 0
    public boolean checkDestination() {
        int dist;
        dist = Math.abs(this.x - parent.getEnding().getX()) + Math.abs(this.y - parent.getEnding().getY());
        if (dist <= 1) {
            isCompleted = true;
            this.next = parent;
        }
        return isCompleted;
    }

    //generates next segment
    public boolean generateNext() {
        if (checkDestination()) {
            return false;
        }
        int[] coordinates = generateNextCoordinates();
        next = new Segment(coordinates[0], coordinates[1], this, parent);
        parent.addSegment((Segment) next);
        return true;
    }

    public boolean generateNextMutation() {
        if (checkDestination()) {
            return false;
        }
        int[] lengths = generateNextCoordinatesMutation();
        int incrX;
        int incrY;
        int pathLength;
        if (lengths[0] != 0) {
            pathLength = Math.abs(lengths[0]);
            incrY = 0;
            if (lengths[0] > 0) incrX = 1;
            else incrX = -1;
        } else {
            pathLength = Math.abs(lengths[1]);
            incrX = 0;
            if (lengths[1] > 0) incrY = 1;
            else incrY = -1;
        }
        next = new Segment(x + incrX, y + incrY, this, parent);
        Segment curr = (Segment) next;
        parent.addSegment((Segment) next);
        for (int i = 1; i < pathLength; i++) {
            if (curr.checkDestination()) {
                return false;
            }
            curr.next = new Segment(curr.x + incrX, curr.y + incrY, curr, parent);
            parent.addSegment((Segment) curr.next);
            curr = (Segment) curr.getNext();
        }
        return true;
    }

    public int[] generateNextCoordinatesMutation() {
        AllowedDirections ad = allowedDirections();
        Random rand = new Random();
        boolean changeX = rand.nextBoolean();
        int xLength = rand.nextInt(parent.getBoardSizeX() + 1) - x;
        int yLength = rand.nextInt(parent.getBoardSizeY() + 1) - y;
        switch (ad) {
            case UP_LEFT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength >= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = -rand.nextInt(x);
                    }
                } else {
                    xLength = 0;
                    while (yLength <= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = rand.nextInt(parent.getBoardSizeY() + 1 - y);
                    }
                }
            }
            case LEFT -> {
                yLength = 0;
                while (xLength >= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                    xLength = -rand.nextInt(x);
                }
            }
            case RIGHT -> {
                yLength = 0;
                while (xLength <= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                    xLength = rand.nextInt(parent.getBoardSizeX() + 1 - x);
                }
            }
            case UP -> {
                xLength = 0;
                while (yLength <= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                    yLength = rand.nextInt(parent.getBoardSizeY() + 1 - y);
                }
            }
            case DOWN -> {
                xLength = 0;
                while (yLength >= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                    yLength = -rand.nextInt(y);
                }
            }
            case UP_DOWN -> {
                xLength = 0;
                while (yLength == 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                    yLength = rand.nextInt(parent.getBoardSizeY() + 1) - y;
                }
            }
            case RIGHT_LEFT -> {
                yLength = 0;
                while (xLength == 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                    xLength = rand.nextInt(parent.getBoardSizeX() + 1) - x;
                }
            }
            case UP_DOWN_LEFT_RIGHT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength == 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = rand.nextInt(parent.getBoardSizeX() + 1) - x;
                    }
                } else {
                    xLength = 0;
                    while (yLength == 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = rand.nextInt(parent.getBoardSizeY() + 1) - y;
                    }
                }
            }
            case UP_RIGHT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength <= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = rand.nextInt(parent.getBoardSizeX() + 1 - x);
                    }
                } else {
                    xLength = 0;
                    while (yLength <= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = rand.nextInt(parent.getBoardSizeY() + 1 - y);
                    }
                }
            }
            case DOWN_LEFT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength >= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = -rand.nextInt(x);
                    }
                } else {
                    xLength = 0;
                    while (yLength >= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = -rand.nextInt(y);
                    }
                }
            }
            case DOWN_RIGHT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength <= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = rand.nextInt(parent.getBoardSizeX() + 1 - x);
                    }
                } else {
                    xLength = 0;
                    while (yLength >= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = -rand.nextInt(y);
                    }
                }
            }
            case UP_DOWN_LEFT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength >= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = -rand.nextInt(x);
                    }
                } else {
                    xLength = 0;
                    while (yLength == 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = rand.nextInt(parent.getBoardSizeY() + 1) - y;
                    }
                }
            }
            case UP_DOWN_RIGHT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength <= 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = rand.nextInt(parent.getBoardSizeX() + 1 - x);
                    }
                } else {
                    xLength = 0;
                    while (yLength == 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = rand.nextInt(parent.getBoardSizeY() + 1) - y;
                    }
                }
            }
            case UP_LEFT_RIGHT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength == 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = rand.nextInt(parent.getBoardSizeX() + 1) - x;
                    }
                } else {
                    xLength = 0;
                    while (yLength <= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = rand.nextInt(parent.getBoardSizeY() + 1 - y);
                    }
                }
            }
            case DOWN_LEFT_RIGHT -> {
                if (changeX) {
                    yLength = 0;
                    while (xLength == 0 || x+xLength<=0 || x+xLength>parent.getBoardSizeX()) {
                        xLength = rand.nextInt(parent.getBoardSizeX() + 1) - x;
                    }
                } else {
                    xLength = 0;
                    while (yLength >= 0 || y+yLength<=0 || y+yLength>parent.getBoardSizeY()) {
                        yLength = -rand.nextInt(y);
                    }
                }
            }
        }
        return new int[]{xLength, yLength};
    }

    public int[] generateNextCoordinates() {
        //needed to choose which variable to modify
        boolean changeX;
        Random rand = new Random();
        changeX = rand.nextBoolean();
        //choosing the direction
        int nextX = -1;
        int nextY = -1;
        Direction dir = direction();
        switch (dir) {
            case UP -> {
                nextX = this.x;
                nextY = this.y + 1;
            }
            case DOWN -> {
                nextX = this.x;
                nextY = this.y - 1;
            }
            case LEFT -> {
                nextX = this.x - 1;
                nextY = this.y;
            }
            case RIGHT -> {
                nextX = this.x + 1;
                nextY = this.y;
            }
            case UP_LEFT -> {
                if (changeX) {
                    nextX = this.x - 1;
                    nextY = this.y;
                } else {
                    nextX = this.x;
                    nextY = this.y + 1;
                }
            }
            case UP_RIGHT -> {
                if (changeX) {
                    nextX = this.x + 1;
                    nextY = this.y;
                } else {
                    nextX = this.x;
                    nextY = this.y + 1;
                }
            }
            case DOWN_LEFT -> {
                if (changeX) {
                    nextX = this.x - 1;
                    nextY = this.y;
                } else {
                    nextX = this.x;
                    nextY = this.y - 1;
                }
            }
            case DOWN_RIGHT -> {
                if (changeX) {
                    nextX = this.x + 1;
                    nextY = this.y;
                } else {
                    nextX = this.x;
                    nextY = this.y - 1;
                }
            }
        }
        return new int[]{nextX, nextY};
    }

    //returns the direction of the finish point
    public Direction direction() {
        if (this.x == parent.getEnding().getX()) {
            if (this.y < parent.getEnding().getY()) return Direction.UP;
            if (this.y > parent.getEnding().getY()) return Direction.DOWN;
        }
        if (this.y == parent.getEnding().getY()) {
            if (this.x < parent.getEnding().getX()) return Direction.RIGHT;
            if (this.x > parent.getEnding().getX()) return Direction.LEFT;
        }
        if (this.y < parent.getEnding().getY()) {
            if (this.x < parent.getEnding().getX()) return Direction.UP_RIGHT;
            if (this.x > parent.getEnding().getX()) return Direction.UP_LEFT;
        }
        if (this.y > parent.getEnding().getY()) {
            if (this.x < parent.getEnding().getX()) return Direction.DOWN_RIGHT;
            if (this.x > parent.getEnding().getX()) return Direction.DOWN_LEFT;
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Object getPrev() {
        return prev;
    }

    public Object getNext() {
        return next;
    }

    public void setPrev(Object o) {
        prev = o;
    }

    @Override
    public String toString() {
        return "segment: (" + this.x + ", " + this.y + ")";
    }

    public AllowedDirections allowedDirections() {
        if (prev instanceof Point) {
            if (x == 1) {
                if (y == 1) return AllowedDirections.UP_RIGHT;
                if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN_RIGHT;
                else return AllowedDirections.UP_DOWN_RIGHT;
            }
            if (y == 1) {
                if (x == 1) return AllowedDirections.UP_RIGHT;
                if (x == parent.getBoardSizeX()) return AllowedDirections.UP_LEFT;
                else return AllowedDirections.UP_LEFT_RIGHT;
            }
            if (x == parent.getBoardSizeX()) {
                if (y == 1) return AllowedDirections.UP_LEFT;
                if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN_LEFT;
                else return AllowedDirections.UP_DOWN_LEFT;
            }
            if (y == parent.getBoardSizeY()) {
                if (x == 1) return AllowedDirections.DOWN_RIGHT;
                if (x == parent.getBoardSizeX()) return AllowedDirections.DOWN_LEFT;
                else return AllowedDirections.DOWN_LEFT_RIGHT;
            } else return AllowedDirections.UP_DOWN_LEFT_RIGHT;
        } else {
            Segment previous = (Segment) this.prev;
            int differenceInX = x - previous.x;
            int differenceInY = y - previous.y;
            //prev x is greater -> no RIGHT
            if (differenceInX == -1) {
                if (x == 1) {
                    if (y == 1) return AllowedDirections.UP;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN;
                    else return AllowedDirections.UP_DOWN;
                }
                if (y == 1) {
                    if (x == 1) return AllowedDirections.UP;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.UP_LEFT;
                    else return AllowedDirections.UP_LEFT;
                }
                if (x == parent.getBoardSizeX()) {
                    if (y == 1) return AllowedDirections.UP_LEFT;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN_LEFT;
                    else return AllowedDirections.UP_DOWN_LEFT;
                }
                if (y == parent.getBoardSizeY()) {
                    if (x == 1) return AllowedDirections.DOWN;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.DOWN_LEFT;
                    else return AllowedDirections.DOWN_LEFT;
                } else return AllowedDirections.UP_DOWN_LEFT;
            }
            //this x is greater -> no LEFT
            if (differenceInX == 1) {
                if (x == 1) {
                    if (y == 1) return AllowedDirections.UP_RIGHT;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN_RIGHT;
                    else return AllowedDirections.UP_DOWN_RIGHT;
                }
                if (y == 1) {
                    if (x == 1) return AllowedDirections.UP_RIGHT;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.UP;
                    else return AllowedDirections.UP_RIGHT;
                }
                if (x == parent.getBoardSizeX()) {
                    if (y == 1) return AllowedDirections.UP;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN;
                    else return AllowedDirections.UP_DOWN;
                }
                if (y == parent.getBoardSizeY()) {
                    if (x == 1) return AllowedDirections.DOWN_RIGHT;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.DOWN;
                    else return AllowedDirections.DOWN_RIGHT;
                } else return AllowedDirections.UP_DOWN_RIGHT;
            }
            //prev y is greater -> no UP
            if (differenceInY == -1) {
                if (x == 1) {
                    if (y == 1) return AllowedDirections.RIGHT;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN_RIGHT;
                    else return AllowedDirections.DOWN_RIGHT;
                }
                if (y == 1) {
                    if (x == 1) return AllowedDirections.RIGHT;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.LEFT;
                    else return AllowedDirections.RIGHT_LEFT;
                }
                if (x == parent.getBoardSizeX()) {
                    if (y == 1) return AllowedDirections.LEFT;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.DOWN_LEFT;
                    else return AllowedDirections.DOWN_LEFT;
                }
                if (y == parent.getBoardSizeY()) {
                    if (x == 1) return AllowedDirections.DOWN_RIGHT;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.DOWN_LEFT;
                    else return AllowedDirections.DOWN_LEFT_RIGHT;
                } else return AllowedDirections.DOWN_LEFT_RIGHT;
            }
            //this y is greater -> no DOWN
            if (differenceInY == 1) {
                if (x == 1) {
                    if (y == 1) return AllowedDirections.UP_RIGHT;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.RIGHT;
                    else return AllowedDirections.UP_RIGHT;
                }
                if (y == 1) {
                    if (x == 1) return AllowedDirections.UP_RIGHT;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.UP_LEFT;
                    else return AllowedDirections.UP_LEFT_RIGHT;
                }
                if (x == parent.getBoardSizeX()) {
                    if (y == 1) return AllowedDirections.UP_LEFT;
                    if (y == parent.getBoardSizeY()) return AllowedDirections.LEFT;
                    else return AllowedDirections.UP_LEFT;
                }
                if (y == parent.getBoardSizeY()) {
                    if (x == 1) return AllowedDirections.RIGHT;
                    if (x == parent.getBoardSizeX()) return AllowedDirections.LEFT;
                    else return AllowedDirections.RIGHT_LEFT;
                } else return AllowedDirections.UP_LEFT_RIGHT;
            }
        }
        return null;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    //todo maybe add copy() method
}
