package io.github.umajack;

import java.util.ArrayList;
import java.util.Collections;

public class Blackjack {
    private ArrayList<Player> players;
    private ArrayList<Float> deck;
    private int dealer = 0;
    private int currentPlayerIndex = 0;
    private boolean gameOver = false;
    private boolean dealerTurn = false;

    public Blackjack(ArrayList<Player> players) {
        this.players = players;
        deck = new ArrayList<>();
        init();
        shuffle();
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

    // Start a new hand and set up for UI-driven play
    public void simulateHand() {
        deal();
        currentPlayerIndex = 0;
        gameOver = false;
        dealerTurn = false;
        System.out.println("Player " + currentPlayerIndex + "'s turn. Hand: " + players.get(currentPlayerIndex).getHand());
    }

    // Call this when the player clicks "Hit"
    public void playerHit() {
        if (gameOver || dealerTurn) return;
        Player p = players.get(currentPlayerIndex);
        float card = hit();
        p.setHand(p.getHand() + v(card));
        System.out.println("Player " + currentPlayerIndex + " hits: " + v(card) + ". Hand: " + p.getHand());
        if (p.getHand() > 21) {
            System.out.println("Player " + currentPlayerIndex + " busted!");
            nextPlayer();
        }
    }

    // Call this when the player clicks "Stand"
    public void playerStand() {
        if (gameOver || dealerTurn) return;
        System.out.println("Player " + currentPlayerIndex + " stands. Hand: " + players.get(currentPlayerIndex).getHand());
        nextPlayer();
    }

    // Move to next player or dealer
    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            dealerTurn = true;
            dealerPlay();
        } else {
            System.out.println("Player " + currentPlayerIndex + "'s turn. Hand: " + players.get(currentPlayerIndex).getHand());
        }
    }

    // Dealer's turn logic
    private void dealerPlay() {
        int max = 0;
        for (Player p : players) {
            int hand = p.getHand();
            if (hand <= 21 && hand > max) {
                max = hand;
            }
        }
        System.out.println("Dealer's hand: " + dealer);
        while (dealer < 17 && dealer < max) {
            float card = hit();
            dealer += v(card);
            System.out.println("Dealer drew " + v(card) + ". Dealer's hand: " + dealer);
        }
        showResults();
    }

    // Show results for each player
    private void showResults() {
        gameOver = true;
        for (int i = 0; i < players.size(); ++i) {
            int hand = players.get(i).getHand();
            System.out.print("Player " + i + "'s result: ");
            if (hand > 21) {
                System.out.println("Busted. Lose.");
            } 
            else if (dealer > 21) {
                System.out.println("Dealer busted. Win.");
            } 
            else if (hand > dealer) {
                System.out.println("Win.");
            } 
            else if (hand == dealer) {
                System.out.println("Push (tie).");
            } 
            else {
                System.out.println("Lose.");
            }
        }
    }
}