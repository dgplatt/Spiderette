package com.main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int num_repeat = 8;
        int[] values = new int[] {2, 4, 0, 0, 3};

        Spiderette game = new Spiderette();
        String comm = "";
        System.out.print("Testng (y/n)?:  ");
        while (! comm.equals("y") && ! comm.equals("n")) {
            comm = input.nextLine();
        }
        if (comm.equals("y")) {
            game.testing = true;
            for (int i = 0; i < 10000; i++) {
                System.out.println(i);
                game.auto_play(num_repeat, values);
            }
            // print stats
            for (int i = 0; i < 200; i ++) {
                if (game.starter_scores[i] > 0) {
                    System.out.println(i + ": Won " + game.winning_starter_scores[i]+ " out of " + game.starter_scores[i] + " games!");
                }
            }
        } else {
            comm = "";
            do {
                System.out.print("Auto (y/n)?:  ");
                while (! comm.equals("y") && ! comm.equals("n")) {
                    comm = input.nextLine();
                }
                if (comm.equals("y")) {
                    game.auto_play(num_repeat, values);
                } else {
                    game.play(num_repeat, values);
                }
                comm = "";
            } while (game.repeat());
        }
        input.close();
        Spiderette.input.close();
        System.out.println("You Won " + game.num_won + " out of " + game.games_played + " Games Played!!!");
    }
}