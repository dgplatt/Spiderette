package com.main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.*; 

public class Main {
    public static void main(String[] args) {
        int num_repeat = 12;
        int[] values = new int[] {2, 4, 0, 0, 3};

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
            for (int i = 0; i < 10000; i++) {
                System.out.println(i);
                game = new Spiderette(values, num_repeat);
                game.auto_play();
                gamesPlayed ++;
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
                if (comm.equals("y")) {
                    game.auto_play();
                } else {
                    game.play();
                }
                if (game.isWinner()) {
                    numWon ++;
                    System.out.println("**** Winner in " + game.getNumMoves() + " moves ****");
                }
                comm = "";
            } while (Spiderette.repeat());
        }
        input.close();
        System.out.println("You Won " + numWon + " out of " + gamesPlayed + " Games Played!!!");
    }
}