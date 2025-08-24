package io.github.umajack;

import java.util.ArrayList;
import java.util.Collections;

public class bj {
    // REVISED SIMPLIFIED SINGLE-PLAYER TURN-BASED VERSION

    private ArrayList<Float> deck;
    private int dealer;
    private int player;
    
    public bj() {
        dealer = 0;
        player = 0;
        init();
        shuffle();
    }

    private void init() {
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

    


}
