package com.main;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import com.auto.Values;

public class Main {
    public static void main(String[] args) throws Exception{
        int num_repeat = 10;
        int number_play = 1;
        int spaceValue = 2;
        int unknownValue = 3;
        int disorderedValue = 0;
        int orderedValue = -1;
        int suitedValue = -4;
        Values values = new Values(spaceValue,unknownValue,disorderedValue,orderedValue,suitedValue);

        String outputPath = "C:\\Users\\Big D\\Documents\\Spiderette\\" + UUID.randomUUID().toString();
        File outputFolder = new File(outputPath);
        outputFolder.mkdir();

        Scanner input = new Scanner(System.in);
        Spiderette.setInput(input);
        int numWon = 0;
        int gamesPlayed = 0;
        ArrayList<int[]> losing_scores = new ArrayList<>();
        ArrayList<int[]> winning_scores = new ArrayList<>();

        Spiderette game;
        String comm = "";

        System.out.print("Testng (y/n)?:  ");
        while (! comm.equals("y") && ! comm.equals("n")) {
            comm = input.nextLine();
        }

        if (comm.equals("y")) {
            System.out.print("Number of objects to test?:  ");
            comm = "";
            while (!isInteger(comm)) {
                comm = input.nextLine();
            }
            number_play = Integer.parseInt(comm);
            for (int gameNum = 0; gameNum < number_play; gameNum++) {
                System.out.println(gameNum);
                game = new Spiderette(values, num_repeat);
                try {
                    game.auto_play();
                } catch(Exception e) {
                    System.out.println("Exception");
                    e.printStackTrace();
                }
                gamesPlayed ++;
                File gameFile = new File(outputPath + "\\" + gameNum + ".txt");
                gameFile.createNewFile();
                FileWriter myWriter = new FileWriter(outputPath + "\\" + gameNum + ".txt");
                myWriter.write(game.getGameRecord());
                myWriter.close();
                if (game.isWinner()) {
                    System.out.println("**** Winner in " + game.getNumMoves() + " moves ****");
                    numWon ++;
                    winning_scores.add(game.getScores());
                } else {
                    losing_scores.add(game.getScores());
                }
            }
            // print stats
            double[] averages = new double[5];

            int[] maxes = new int[5];
            int[] mins = new int[5];
            for (int i = 0; i < 5; i ++) {
                maxes[i] = losing_scores.get(0)[i];
                mins[i] = losing_scores.get(0)[i];
            }
            for (int[] temScores: losing_scores) {
                for (int i = 0; i < 5 ; i++) {
                    if(temScores[i] > maxes[i]) {
                        maxes[i] = temScores[i];
                    } else if(temScores[i] < mins[i]) {
                        mins[i] = temScores[i];
                    }
                    averages[i] += temScores[i];
                }
            }
            for (int i = 0; i < 5; i ++) {
                averages[i] = averages[i]/losing_scores.size();
            }
            System.out.println("Losing Mins: " + Arrays.toString(mins));
            System.out.println("Losing Averages: " + Arrays.toString(averages));
            System.out.println("Losing Maxes: " + Arrays.toString(maxes));
            averages = new double[5];
            maxes = new int[5];
            mins = new int[5];
            for (int i = 0; i < 5; i ++) {
                maxes[i] = winning_scores.get(0)[i];
                mins[i] = winning_scores.get(0)[i];

            }
            for (int[] temScores: winning_scores) {
                for (int i = 0; i < 5 ; i++) {
                    if(temScores[i] > maxes[i]) {
                        maxes[i] = temScores[i];
                    } else if(temScores[i] < mins[i]) {
                        mins[i] = temScores[i];
                    }
                    averages[i] += temScores[i];
                }
            }
            for (int i = 0; i < 5; i ++) {
                averages[i] = averages[i]/winning_scores.size();
            }
            System.out.println("Winning Mins: " + Arrays.toString(mins));
            System.out.println("Winning Averages: " + Arrays.toString(averages));
            System.out.println("Winning Maxes: " + Arrays.toString(maxes));

        } else {
            comm = "";
            do {
                game = new Spiderette(values, num_repeat);
                System.out.print("Auto (y/n)?:  ");
                while (! comm.equals("y") && ! comm.equals("n")) {
                    comm = input.nextLine();
                }
                boolean auto_play = comm.equals("y");
                if (auto_play) {
                    game.auto_play();
                } else {
                    game.play();
                }
                if (game.isWinner()) {
                    numWon ++;
                    if(auto_play) {
                        System.out.println(game.getGameRecord());
                    }
                    System.out.println("**** Winner in " + game.getNumMoves() + " moves ****");
                }
                comm = "";
            } while (Spiderette.repeat());
        }
        input.close();
        System.out.println("You Won " + numWon + " out of " + gamesPlayed + " Games Played!!!");
    }

    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}