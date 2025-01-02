import java.util.ArrayList;

/**
 * Class representing the logic of the dice game.
 */
public class GamePlay {
    /**
     * Simulates the dice game and produces the game output.
     *
     * @param numberOfPlayers the number of players in the game
     * @param players          the list of player names
     * @param content          the list of strings containing game content
     * @return a list of strings representing the game output
     */
    public static ArrayList<String> game(int numberOfPlayers, ArrayList<String> players, ArrayList<String> content) {
        int[] scores = new int[numberOfPlayers];
        boolean[] eliminated = new boolean[numberOfPlayers];
        int currentPlayerIndex = 0;

        ArrayList<String> output = new ArrayList<>();

        while (countActivePlayers(eliminated) > 1) {
            if (!eliminated[currentPlayerIndex]) {
                String line = content.get(0);
                content.remove(0);
                if (line.equals("0-0")) {
                    output.add(players.get(currentPlayerIndex) + " skipped the turn and " +
                            players.get(currentPlayerIndex) + "’s score is " + scores[currentPlayerIndex] + ".");
                } else {
                    String[] diceValues = line.split("-");
                    int dice1 = Integer.parseInt(diceValues[0]);
                    int dice2 = Integer.parseInt(diceValues[1]);

                    if (dice1 == 1 && dice2 == 1) {
                        eliminated[currentPlayerIndex] = true;
                        output.add(players.get(currentPlayerIndex) + " threw 1-1. Game over " +
                                players.get(currentPlayerIndex) + "!");
                    } else if (dice1 == 1 || dice2 == 1) {
                        output.add(players.get(currentPlayerIndex) + " threw " + line +
                                " and " + players.get(currentPlayerIndex) + "’s score is " +
                                scores[currentPlayerIndex] + ".");
                    } else {
                        scores[currentPlayerIndex] += dice1 + dice2;
                        output.add(players.get(currentPlayerIndex) + " threw " + line +
                                " and " + players.get(currentPlayerIndex) + "’s score is " +
                                scores[currentPlayerIndex] + ".");
                    }
                }
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            if (!eliminated[i]) {
                output.add(players.get(i) + " is the winner of the game with the score of " +
                        scores[i] + ". Congratulations " + players.get(i) + "!");
                break;
            }
        }

        return output;
    }
    /**
     * Calculates the number of active players (players who have not been eliminated).
     *
     * @param eliminated an array indicating whether each player is eliminated
     * @return the number of active players
     */

    private static int countActivePlayers(boolean[] eliminated) {
        int count = 0;
        for (boolean isEliminated : eliminated) {
            if (!isEliminated) {
                count++;
            }
        }
        return count;
    }
}
