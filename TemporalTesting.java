package genetic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TemporalTesting {
    public static void main(String[] args) {
        //todo IMPORTANT: 0 IS NOT ACCEPTED
        int size[] = {20,20};
        int pairs[] = {
        1,10,19,10,
        4,1,4,16,
        8,4,8,20,
        12,1,12,16,
        16,4,16,20};
        //todo data is wrong (zad 1)
        GA ga = new GA(size, pairs, 1000, true, 100, 999);
        ga.firstGeneration();
        //draw(ga.getBest());
        for (int i = 0; i <200; i++) {
           ga.nextGeneration();
           if(i%100==0)
           draw(ga.getBoards().get(ga.getIndexOfTheBest()));
        }
        draw(ga.getBoards().get(ga.getIndexOfTheBest()));
        draw(ga.getBoards().get(ga.getIndexOfTheWorst()));
        //draw(ga.getBest());
        //todo smth is wrong
        //System.out.println("Best fitness in total: "+ga.getBest().getFitness());
    }

    public static class ToCanvas extends Component {
        Board b;

        public ToCanvas(Board b) {
            this.b = b;
        }

        public void paint(Graphics g) {
            Random rnd = new Random();
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(50, 290 - b.getSizeY() * 10, b.getSizeX() * 10 + 10, b.getSizeY() * 10 + 10);
            g2.setStroke(new BasicStroke(2));
            for (Path p : b.getPairs()) {
                g2.setColor(new Color(100 + rnd.nextInt(155) , 100 + rnd.nextInt(155) , 100 + rnd.nextInt(155) ));
                drawOval(g2, p.getEnding().getX(),p.getEnding().getY());
                drawOval(g2, p.getStarting().getX(), p.getStarting().getY());
                ArrayList<Segment> seg = p.getSegments();

                drawLine(g2, p.getStarting().getX(), p.getStarting().getY(), seg.get(0).getX(), seg.get(0).getY());
                for (int i = 1; i < seg.size(); i++) {
                    drawLine(g2, seg.get(i - 1).getX(), seg.get(i - 1).getY(), seg.get(i).getX(), seg.get(i).getY());
                }
                drawLine(g2, seg.get(seg.size() - 1).getX(), seg.get(seg.size() - 1).getY(), p.getEnding().getX(), p.getEnding().getY());
            }
        }

        public void drawOval(Graphics2D g2, int x1, int y1) {
            int w = 8;
            int h = 8;
            g2.fillOval(transformToCoordinates(x1, true) - w/2, transformToCoordinates(y1, false) - h /2, w, h);

        }
        public void drawLine(Graphics2D g2, int x1, int y1, int x2, int y2) {
            g2.drawLine(transformToCoordinates(x1, true), transformToCoordinates(y1, false), transformToCoordinates(x2, true), transformToCoordinates(y2, false));


        }
    }

    public static void draw(Board b) {
        Frame frame = new Frame();
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.add(new ToCanvas(b));
    }

    public static int transformToCoordinates(int coord, boolean itIsX) {
        if (itIsX) {
            return 50 + coord * 10;
        } else {
            return 300 - coord * 10;
        }
    }
}
