package genetic;

import java.util.ArrayList;
import java.util.Random;
public class Path {
    private final int boardSizeX;
    private final int boardSizeY;
    private final Point starting;
    private final Point ending;
    private ArrayList<Segment> segments = new ArrayList<Segment>();

    public Path(int x1, int y1, int x2, int y2, int boardSizeX, int boardSizeY) {
        starting = new Point(x1, y1);
        ending = new Point(x2, y2);
        this.boardSizeX = boardSizeX;
        this.boardSizeY = boardSizeY;
    }
    //path generation
    public void generatePath(){
        //generating the first one that would be removed in future (because it is placed right on the starting point coordinates)
        segments.add(new Segment(this.starting.getX(), this.starting.getY(), starting, this));
        Segment curr = segments.get(0);
        while(curr.generateNext()){
            curr = (Segment) curr.getNext();
        }
        //removing the first one
        segments.remove(0);
        segments.get(0).setPrev(starting);
    }
    //needed for segment generation
    public void addSegment(Segment s) {
        segments.add(s);
    }

    public Point getEnding() {
        return ending;
    }

    public Point getStarting() {
        return starting;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        String s = "start\n";
        s+=starting+"\n";
        for(Segment seg: segments){
            s+=seg+"\n";
        }
        s+=ending+"\n";
        s+="end";
        return s;
    }

    //todo version control
    //todo new mutation (moving lines, not recreating the whole paths)
    //todo mutate a lot of times restricting the starting point
    public void mutateStrong(){
        Random rand = new Random();
        int whereToStart = rand.nextInt(segments.size());
        if(rand.nextInt(11)>9) whereToStart = 1;
        while(segments.size()>whereToStart+1){
            segments.remove(whereToStart+1);
        }
        Segment curr = segments.get(whereToStart);
        if(curr.generateNextMutation()) {
            curr = segments.get(segments.size() - 1);
            while(curr.generateNext()){
                curr = (Segment) curr.getNext();
            }
        }
        int currPoint = whereToStart;
        int counter = 0;
        double nextMutation = rand.nextDouble();
        whereToStart += rand.nextInt(segments.size()-currPoint);
        while(whereToStart<segments.size()-1 && counter<5 && nextMutation>0.6){
            while(segments.size()>whereToStart+1){
                segments.remove(whereToStart+1);
            }
            curr = segments.get(whereToStart);
            if(curr.generateNextMutation()) {
                curr = segments.get(segments.size() - 1);
                while(curr.generateNext()){
                    curr = (Segment) curr.getNext();
                }
                nextMutation = rand.nextDouble();
            }
            currPoint = whereToStart;
            whereToStart += rand.nextInt(segments.size()-currPoint);
            counter++;
        }
    }

    public int getBoardSizeX() {
        return boardSizeX;
    }

    public int getBoardSizeY() {
        return boardSizeY;
    }

    @Override
    public Path clone(){
        int startX = starting.getX();
        int startY = starting.getY();
        int endX = ending.getX();
        int endY = ending.getY();
        Path newPath = new Path(startX, startY, endX, endY, boardSizeX, boardSizeY);
        ArrayList<Segment> newSegments = new ArrayList<>();
        Segment s0 = new Segment(segments.get(0).getX(), segments.get(0).getY(), newPath.getStarting(), newPath);
        newSegments.add(s0);
        for(int i = 1; i < segments.size(); i++){
            Segment s = new Segment(segments.get(i).getX(), segments.get(i).getY(), newSegments.get(i-1), newPath);
            newSegments.add(s);
        }
        for(int i = 0; i < newSegments.size()-1; i++){
            newSegments.get(i).setNext(newSegments.get(i+1));
        }
        newSegments.get(newSegments.size()-1).setNext(newPath.getEnding());
        for(Segment s: newSegments){
            newPath.segments.add(s);
        }
        return newPath;
        //todo check
    }
}
