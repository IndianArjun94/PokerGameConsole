package Game;

public class Card {
    private int rank;
    private int suit;
    private int value;

    public Card() {

    }

    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
        update();
    }

    public int rank(int rank) {
        this.rank = rank;
        update();
        return rank;
    }

    public int rank() {
        return rank;
    }

    public int suit(int suit) {
        this.suit = suit;
        update();
        return suit;
    }

    public int suit() {
        return suit;
    }

    private void update() {
        if (rank != 1) {
            value = rank-1;
        } else {
            value = 13;
        }
    }

    public int value() {
        return value;
    }
}
