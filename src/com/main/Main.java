package com.main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int num_repeat = 10;
        int[] values = new int[] {2, 4, 0, 0, 3};

        Scanner input = new Scanner(System.in);
        Spiderette.setInput(input);
        int numWon = 0;
        int gamesPlayed = 0;
        int[] starter_scores = new int[300];
        int[] winning_starter_scores = new int[300];

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
                starter_scores[game.getNoDealScore()] ++;
                gamesPlayed ++;
                if (game.isWinner()) {
                    System.out.println("**** Winner in " + game.getNumMoves() + " moves ****");
                    numWon ++;
                    winning_starter_scores[game.getNoDealScore()] ++;
                }
            }
            // print stats
            for (int i = 0; i < 200; i ++) {
                if (starter_scores[i] > 0) {
                    System.out.println(i + ": Won " + winning_starter_scores[i]+ " out of " + starter_scores[i] + " games!");
                }
            }
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