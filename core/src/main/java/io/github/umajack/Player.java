package io.github.umajack;

public class Player {
    private float balance;
    private int hand;

    public Player() {
        balance = 10000;
    }

    public Player(float balance) {
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getHand() {
        return hand;
    }
    
    public void setHand(int hand) {
        this.hand = hand;
    }
}
