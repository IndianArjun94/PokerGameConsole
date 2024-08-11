package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Game implements Runnable {
    public Card[] playerCards = new Card[2];
    public Card[] computerCards = new Card[2];
    public Card[] communityCards = new Card[5];

    public boolean isPlayerBigBlind = false;
    public boolean checkProposed = false;

    public LinkedList<Integer> usedCards = new LinkedList<>();

    public int playerBetAmount = 5;
    public int computerBetAmount = 5;
    public int pot = 0;
    public int bettingRound = 0;

    public Game() {
//        Deal cards for player & computer
        playerCards[0] = newRandomCard();
        playerCards[1] = newRandomCard();
        computerCards[0] = newRandomCard();
        computerCards[1] = newRandomCard();

//        Set Community Cards
        communityCards[0] = newRandomCard();
        communityCards[1] = newRandomCard();
        communityCards[2] = newRandomCard();
        communityCards[3] = newRandomCard();
        communityCards[4] = newRandomCard();

//        TODO: Randomize isPlayerBigBlind either true or false

        if (isPlayerBigBlind) {
            playerBetAmount = playerBetAmount + 5;
        } else {
            computerBetAmount = computerBetAmount + 5;
        }
    }

    private void bet() {
        Thread bettingThread = new Thread(this);
        bettingThread.start();
    }

    public void gameLoop() {
        bettingRound++;

        bet();

        bettingRound++;

        bet();

        bettingRound++;

        bet();

        bettingRound++;

        bet();

//        gameOver();
    }

    private boolean fourOfAKind(Card[] list) {
        int number;
        int length = list.length;
        int streak = 0;
        boolean win = false;

        for (int i = 0; i < length; i++) {
            number = list[i].rank();
            list[i].rank(100);

            for (Card currentCard : list) {
                if (number == currentCard.rank()) {
                    if (streak == 2) {
                        win = true;
                        streak = 0;
                        break;
                    } else {
                        streak++;
                    }
                }
            }
        }

        return win;
    }

    private int checkPairs(Card[] list, boolean threeOfAKind) {
        int number;
        int length = list.length;
        int pairs = 0;
        int streak = 0;

        for (int i = 0; i < length; i++) {
            number = list[i].rank();
            list[i].rank(100);

            for (Card currentCard : list) {
                if (number == currentCard.rank()) {
                    if (threeOfAKind) {
                        if (streak == 1) {
                            pairs++;
                            streak = 0;
                            break;
                        } else {
                            streak++;
                        }
                    } else {
                        pairs++;
                        break;
                    }
                }
            }
        }

        return pairs;
    }

    private int getComputerCardsValue() {
        int value = 0;

        if (computerCards[0].value() > 7 || computerCards[1].value() > 7) { // High Card
            value = 1;
        } if (computerCards[0].rank() == computerCards[1].rank()) { // One Pair
            value = 2;
        } if (bettingRound >= 2) {
            Card[] listOfCards = new Card[0];

            if (bettingRound == 2) {
                listOfCards = new Card[]{computerCards[0], computerCards[1], communityCards[0], communityCards[1], communityCards[2]};
            } else if (bettingRound == 3) {
                listOfCards = new Card[]{computerCards[0], computerCards[1], communityCards[0], communityCards[1], communityCards[2], communityCards[3]};
            } else if (bettingRound == 4) {
                listOfCards = new Card[]{computerCards[0], computerCards[1], communityCards[0], communityCards[1], communityCards[2], communityCards[3], communityCards[4]};
            }

            if (checkPairs(listOfCards, false) == 1) { // One Pair
                value = 2;
            } if (checkPairs(listOfCards, false) == 2) { // Two Pair
                value = 3;
            } if (checkPairs(listOfCards, true) >= 1) { // Three Pair
                value = 4;
            } if (checkPairs(listOfCards, false) >= 1 && checkPairs(listOfCards, true) >= 1) {
                value = 7;
            }

            if (bettingRound >= 3) {
                int streak = 0;
                int prev = listOfCards[0].suit();
                int[] list = new int[listOfCards.length];
                int i = 0;
                for (Card card : listOfCards) {
                    if (card.suit() == prev) {
                        streak++;
                    }
                    prev = card.suit();
                    list[i] = card.rank();
                    i++;
                }

                if (streak >= 5) { // Flush
                    value = 6;
                }

                Arrays.sort(list);
                int prevElement = 0;
                int straight = 2;
                for (int element : list) {
                    if (prevElement == element && straight != 0) {
                        straight = 1;
                    } else {
                        straight = 0;
                    }
                }

                if (straight == 1) { // Straight
                    if (value == 6) {
                        value = 9;
                    } else {
                        value = 5;
                    }
                }

                if (checkPairs(listOfCards, false) >= 1 && checkPairs(listOfCards, true) >= 1) { // Full House
                    value = 7;
                }

                if (fourOfAKind(listOfCards)) { // Four Of A Kind
                    value = 8;
                }

                ArrayList<Integer> arrayList = new ArrayList<>();

                for (int asd : list) {
                    arrayList.add(asd);
                }

                if (value == 6 && arrayList.contains(10) && arrayList.contains(1) && arrayList.contains(12) && arrayList.contains(13) && arrayList.contains(1)) {
                    value = 10;
                }

            }
        }

        return value;
    }

    public Card newRandomCard() {
        int rank = new Random().nextInt(1, 52);
        while (usedCards.contains(rank)) {
            rank = new Random().nextInt(1, 52);
        }
        usedCards.add(rank);

        int suit;
        Card returnValue = new Card();

        if (rank > 39) {
            rank = rank - 39;
            suit = 4;
        } else if (rank > 26) {
            rank = rank - 26;
            suit = 3;
        } else if (rank > 13) {
            rank = rank - 13;
            suit = 2;
        } else {
            suit = 1;
        }

        returnValue.suit(suit);
        returnValue.rank(rank);
        return returnValue;
    }

    public synchronized void playerBet(int value) {
        playerBetAmount = playerBetAmount + value;
        pot = playerBetAmount + computerBetAmount;
        notifyAll();
    }

    public synchronized void computerBet(int value) {
        computerBetAmount = computerBetAmount + value;
        pot = playerBetAmount + computerBetAmount;
    }

    @Override
    public void run()  {
        boolean bettingOver = false;
        checkProposed = false;
        boolean firstTimeBetting = true;
        int playerPreviousBet;
        int howMuchPlayerBet = 0;

        if (!isPlayerBigBlind) { // Player is small blind, they bet first
            while (!bettingOver) {
//                Waiting for player to bet
                playerPreviousBet = playerBetAmount;

                try {
                    wait();
//                    bettingThread.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                howMuchPlayerBet = playerBetAmount - playerPreviousBet;

//                TODO Computer Betting code goes here
                int computerCardsValue = getComputerCardsValue() * 10;
                int computerConfidence = computerCardsValue - howMuchPlayerBet;

                if (playerBetAmount == 0 && checkProposed) {
                    bettingOver = true;
                } else {
                    if (computerConfidence < 1) { // Fold
                        gameOver("player");
                    } else if (computerConfidence < 6) { // Match
                        computerBet(playerBetAmount - computerBetAmount);
                        if (checkProposed) {
                            bettingOver = true;
                        } else {
                            checkProposed = true;
                        }
                    } else { // Raise
                        computerBet((int) (computerConfidence * 1.5));
                    }
                }
            }
        } else {
            while (!bettingOver) {
//                TODO Computer Betting code goes here
                if (!firstTimeBetting) {
                    int computerCardsValue = getComputerCardsValue() * 10;
                    int computerConfidence = computerCardsValue - howMuchPlayerBet;

                    if (playerBetAmount == 0 && checkProposed) {
                        bettingOver = true;
                    } else {
                        if (computerConfidence < 1) { // Fold
                            gameOver("player");
                        } else if (computerConfidence < 6) { // Match
                            computerBet(playerBetAmount - computerBetAmount);
                            if (checkProposed) {
                                bettingOver = true;
                            } else {
                                checkProposed = true;
                            }
                        } else { // Raise
                            computerBet((int) (computerConfidence * 1.5));
                        }
                    }
                } else {
                    computerBet((int) (getComputerCardsValue() * 1.5));
                    firstTimeBetting = false;
                }

//                Waiting for player to bet
                playerPreviousBet = playerBetAmount;

                try {
                    wait();
//                    bettingThread.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                howMuchPlayerBet = playerBetAmount - playerPreviousBet;
            }
        }
    }

//    public void _wait() {
//        bettingThread.wait();
//    }

    public void gameOver(String winner) {
//        TODO Game Over
    }
}
