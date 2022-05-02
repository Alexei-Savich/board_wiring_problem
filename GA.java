package genetic;

import java.util.ArrayList;
import java.util.Random;

//todo store the best solution as a copy
public class GA {
    ArrayList<Board> boards = new ArrayList<>();
    private final int[] sizes;
    private final int[] pairs;
    private int generation = 0;
    private int indexOfTheBest = -1;
    private int indexOfTheWorst = -1;
    private float averageFitness = -1;
    private final int generationSize;
    private final boolean roulette;
    private final double mutationChance;
    private final int tournamentSize;
    private Board best;

    public GA(int[] s, int[] p, int genSize, boolean roulette, double mutationChance, int tournamentSize) {
        sizes = s;
        pairs = p;
        generationSize = genSize;
        this.roulette = roulette;
        this.mutationChance = mutationChance;
        this.tournamentSize = tournamentSize;
    }

    public void firstGeneration() {
        int lowestFitness = Integer.MAX_VALUE;
        int highestFitness = Integer.MIN_VALUE;
        averageFitness = 0;
        for (int i = 0; i < generationSize; i++) {
            Board b = new Board(sizes, pairs);
            b.generateAllPath();
            //todo here might be a problem
            for (Path p : b.getPairs()) {
                p.mutateStrong();
            }
            int currFitness = b.calculateFitness();
            if (currFitness > highestFitness) {
                highestFitness = currFitness;
                indexOfTheWorst = i;
            }
            if (currFitness < lowestFitness) {
                lowestFitness = currFitness;
                indexOfTheBest = i;
            }
            averageFitness += currFitness;
            boards.add(b);
        }
        averageFitness = averageFitness / generationSize;
        generation++;
        System.out.println("Generation: " + generation + "; best: " + boards.get(indexOfTheBest).getFitness() + "; average: " + averageFitness + "; worst: " + boards.get(indexOfTheWorst).getFitness());
        //todo check if it is the best
        //todo it should be a copy
        best = boards.get(indexOfTheBest).clone();
    }

    //TODO maybe adding the best individuals
    public void nextGeneration() {
        //todo check int lowestFitness = boards.get(indexOfTheBest).calculateFitness();
        int lowestFitness = Integer.MAX_VALUE;
        int highestFitness = Integer.MIN_VALUE;
        averageFitness = 0;
        int indexOfTheWorst = -1;
        int indexOfTheBest = -1;
        ArrayList<Board> temporalBoards = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            //todo crossover should give copies
            Board b = crossover(roulette);
            Random rand = new Random();
            double mutationNumber = rand.nextDouble() * 100;
            if (mutationChance > mutationNumber) {
                mutationNumber = rand.nextInt(b.getPairs().size());
                //todo here the copy, not the reference should be mutated
                b.getPairs().get((int) mutationNumber).mutateStrong();
            }
            temporalBoards.add(b);
            int currFitness = b.calculateFitness();
            if (currFitness > highestFitness) {
                highestFitness = currFitness;
                indexOfTheBest = i;
            }
            if (currFitness < lowestFitness) {
                lowestFitness = currFitness;
                indexOfTheWorst = i;
            }
            averageFitness += currFitness;
            //todo very strange
            if (best.getFitness() > lowestFitness)
                best = boards.get(indexOfTheBest).clone();
        }
/*        temporalBoards.remove(temporalBoards.size() - 1);
        temporalBoards.add(boards.get(this.indexOfTheBest));*/
        averageFitness = averageFitness / generationSize;
        generation++;
        //todo here is the line responsible for printing the results
        if (generation % 100 == 0) {
            System.out.println("Generation: " + generation + "; best: " + lowestFitness + "; average: " + averageFitness + "; worst: " + highestFitness);
        }
        boards.clear();
        for (int i = 0; i < temporalBoards.size(); i++) {
            //todo a copy should be added (which should be created earlier)
            boards.add(temporalBoards.get(i));
        }
        this.indexOfTheBest = indexOfTheBest;
        this.indexOfTheWorst = indexOfTheWorst;
        if (best.getFitness() > lowestFitness) {
            best = boards.get(indexOfTheBest).clone();
        }
    }

    public Board crossover(boolean roulette) {
        Board b1;
        Board b2;
        if (roulette) {
            //todo here should be returned a copy
            b1 = rouletteWheel();
            b2 = rouletteWheel();
        } else {
            //todo here should be returned a copy
            b1 = tournament(tournamentSize);
            b2 = tournament(tournamentSize);
        }
        Board offspring = new Board(sizes, pairs);
        Random rand = new Random();
        int borderPoint = rand.nextInt(b1.getPairs().size());
        ArrayList<Path> p1 = new ArrayList<>();
        ArrayList<Path> p2 = new ArrayList<>();
        //todo a copies are to be added
        for (int i = 0; i < borderPoint; i++) {
            p1.add(b1.getPairs().get(i));
        }
        //todo a copies are to be added
        for (int i = borderPoint; i < b2.getPairs().size(); i++) {
            p2.add(b2.getPairs().get(i));
        }
        offspring.setPairs(new ArrayList<>());
        offspring.addPairs(p1);
        offspring.addPairs(p2);
        //todo there was cloning
        return offspring;
    }

    public Board rouletteWheel() {
        double sum = 0;
        for (Board board : boards) {
            sum = (double) 1 / board.getFitness() + sum;
        }
        Random rand = new Random();
        double partialSum = 0;
        double randNumber = rand.nextDouble() * sum;
        for (Board board : boards) {
            partialSum += (double) 1 / board.getFitness();
            if (partialSum >= randNumber) {
                //todo should return a copy (decide where the copy would be created)
                return board;
            }
        }
        return null;
    }

    //todo redo
    //todo choosing randomly individuals of some number(variable) and choosing the best one
    public Board tournament(int tournamentSize) {
        //todo selecting n random
        Random rand = new Random();
        //todo here is a subset
        int bound1 = rand.nextInt(generationSize - tournamentSize);
        int bestIndex = -1;
        int bestFitness = Integer.MAX_VALUE;
        for (int i = bound1; i <= bound1 + tournamentSize; i++) {
            if (boards.get(i).getFitness() < bestFitness) {
                bestFitness = boards.get(i).getFitness();
                bestIndex = i;
            }
        }
        //todo here is a reference (should be a copy)
        return boards.get(bestIndex);
    }

    public int getIndexOfTheBest() {
        return indexOfTheBest;
    }

    public int getIndexOfTheWorst() {
        return indexOfTheWorst;
    }

    public Board getBoard(int index) {
        return boards.get(index);
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<Board> boards) {
        this.boards = boards;
    }

    public Board getBest() {
        return best;
    }
}
