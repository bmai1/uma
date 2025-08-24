package io.github.umajack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class prelobotomy {
    private ArrayList<Player> players;
    private ArrayList<Float> deck;
    private int dealer = 0;

    public prelobotomy(ArrayList<Player> players) {
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

    public ArrayList<Integer> findWinners() {
        ArrayList<Integer> winners = new ArrayList<>();
        int max = 0;
        for (int player = 0; player < players.size(); ++player) {
            int hand = players.get(player).getHand();
            if (hand > max && hand <= 21) {
                winners.clear();
                winners.add(player);
                max = hand;
            }
            else if (hand == max) {
                winners.add(player);
            }
        }

        // Dealer doesn't bust and ties or all players bust
        if (dealer == max || max == 0) {
            winners.add(-1);
        }

        // Dealer doesn't bust and has highest hand 
        else if (dealer <= 21 && dealer > max) {
            winners.clear();
            winners.add(-1);
        }

        winners.add(max);
        return winners;
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
                    System.out.println("Player " + player + "'s hand: " + p.getHand() + ". Busted.");
                    break;
                }
            }
        }
        scanner.close();

        boolean allBusted = true;
        int max = 0;
        for (Player p : players) {
            int hand = p.getHand();
            if (hand <= 21) {
                allBusted = false;
            }
            if (hand <= 21 && hand > max) {
                max = hand;
            }
        }

        if (allBusted) {
            System.out.println("All players busted. Dealer wins.");
        } 
        else {
            // Dealer tries to beat highest hand
            System.out.println("Dealer's hand: " + dealer);
            while (dealer < 17 && dealer < max) {
                float card = hit();
                dealer += v(card);
                System.out.println("Dealer drew " + v(card) + ". Dealer's hand: " + dealer);
            }

            // Results for each player
            for (int player = 0; player < players.size(); ++player) {
                int playerHand = players.get(player).getHand();
                System.out.print("Player " + player + "'s result: ");
                if (playerHand > 21) {
                    System.out.println("Busted. Lose.");
                } 
                else if (dealer > 21) {
                    System.out.println("Dealer busted. Win.");
                } 
                else if (playerHand > dealer) {
                    System.out.println("Win.");
                } 
                else if (playerHand == dealer) {
                    System.out.println("Push (tie).");
                } 
                else {
                    System.out.println("Lose.");
                }
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player());
        Blackjack b = new Blackjack(players);
        b.simulateHand();
    }
}