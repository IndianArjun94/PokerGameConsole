import Game.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
//        Initialization
        Game game = new Game();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//        Displays player cards
        System.out.println("Your Card: Rank: " + game.playerCards[0].rank() + " Suit: " + game.playerCards[0].suit());
        System.out.println("Your Card: Rank: " + game.playerCards[1].rank() + " Suit: " + game.playerCards[1].suit() + "\n");

        game.gameLoop();

//        First round of betting
        boolean breakLoop = false;
        while (!breakLoop) {
            int prevBetAmountPlayer = game.playerBetAmount;
            game.playerBet(Integer.parseInt(reader.readLine()));

            if (game.checkProposed && game.playerBetAmount - prevBetAmountPlayer == 0) {
                breakLoop = true;
            }

            System.out.println("Computer Total: " + game.computerBetAmount);
        }

//        Displays community cards
        System.out.println("\nCommunity Card: Rank: " + game.communityCards[0].rank() + " Suit: " + game.communityCards[0].suit());
        System.out.println("\nCommunity Card: Rank: " + game.communityCards[1].rank() + " Suit: " + game.communityCards[1].suit());
        System.out.println("\nCommunity Card: Rank: " + game.communityCards[2].rank() + " Suit: " + game.communityCards[2].suit());
    }
}
