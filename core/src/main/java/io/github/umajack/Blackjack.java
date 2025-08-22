package io.github.umajack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Blackjack {
    private ArrayList<Player> players;
    private ArrayList<Float> deck;
    private int dealer = 0;

    public Blackjack(ArrayList<Player> players) {
        this.players = players;
        deck = new ArrayList<>();
        init();
        shuffle();
        // System.out.println("There are " + deck.size() + " cards");
    }

    private void init() {
        // Add cards for each suit
        // Ace (1), Jack (11), Queen (12), King (13)
        for (float rank = 1; rank <= 13; ++rank) {
            for (float suit = 0.1f; suit <= 0.4f; suit += 0.1f) {
                deck.add(rank + suit);
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Float hit() {
        float card = deck.remove(deck.size() - 1);
        if (deck.size() == 0) {
            init();
            shuffle();
        }
        return card;
    }

    public int v(float card) {
        return Math.min((int) card, 10);
    }

    public void bet(Player p, float amount) {
        p.setBalance(p.getBalance() - amount);
    }

    public void deal() {
        for (Player p : players) {
            float card = hit();
            p.setHand(v(card));
        }
        float upcard = hit();
        dealer = v(upcard);

        for (Player p : players) {
            float card = hit();
            p.setHand(p.getHand() + v(card));
        }
        float hole = hit();
        dealer += v(hole);

        // Print hands after deal
        for (int i = 0; i < players.size(); ++i) {
            System.out.println("Player " + i + "'s hand: " + players.get(i).getHand());
        }
        System.out.println("Dealer's upcard: " + (dealer - v(hole)));
    }

    public int findWinner() {
        int min = 21, winner = -1;
        for (int player = 0; player < players.size(); ++player) {
            int diff = 21 - players.get(player).getHand();
            // TODO: handle push for multiple players when equal hands
            if (diff >= 0 && diff <= min) {
                winner = player;
                min = diff;
            }
        }
        int diff = 21 - dealer;
        return (diff <= min) ? -1 : winner;
    }

    public void simulateHand() {
        deal();
        Scanner scanner = new Scanner(System.in);
        for (int player = 0; player < players.size(); ++player) {
            Player p = players.get(player);
            while (true) {
                System.out.print("Player " + player + "'s hand: " + p.getHand() + ". Hit or stand? (h/s): ");
                String input = scanner.nextLine();
                if (input.equals("s")) {
                    break;
                }
                float card = hit();
                p.setHand(p.getHand() + v(card));
                if (p.getHand() > 21) {
                    break;
                }
            }
        }
        scanner.close();

        while (dealer < 17) {
            float card = hit();
            dealer += v(card);
        }

        System.out.println("Dealer's hand: " + dealer);
        System.out.println("The winner is player " + findWinner());
    }

    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player());
        Blackjack b = new Blackjack(players);
        b.simulateHand();
    }
}