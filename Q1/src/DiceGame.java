import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 Main class for the dice game application. Input is read from a file, the game is processed, and output is written to another file.
 */

public class DiceGame {
    /**
     * Main method of the program.
     *
     * @param args command-line arguments. Expects two arguments: input file path and output file path.
     */
    public static void main(String[] args) {
        String[] content = FileInput.readFile(args[0], false, false);
        List<String> contentList = new ArrayList<>(Arrays.asList(content));

        PlayerInfo playerInfo = gamePlayers(contentList);
        int numberOfPlayers = playerInfo.getNumberOfPlayers();


        String[] playerNamesArray = playerInfo.getPlayers().split(",");
        ArrayList<String> players = new ArrayList<>(Arrays.asList(playerNamesArray));


        ArrayList<String> output = GamePlay.game(numberOfPlayers, players, new ArrayList<>(contentList));


        FileOutput.writeToFile(args[1], "", false, false);

        for (String line : output) {
            if(output.indexOf(line) == output.size() - 1) {
                FileOutput.writeToFile(args[1], line, true, false);
            }
            else {
                FileOutput.writeToFile(args[1], line, true, true);
            }
        }
    }

    /**
     * Extracts player information from the input content.
     *
     * @param content the list of strings containing player information
     * @return a PlayerInfo object containing the number of players and their names
     */

    public static PlayerInfo gamePlayers(List<String> content) {
        int numberOfPlayers = Integer.parseInt(content.get(0));
        String playerNames = content.get(1);
        content.remove(1);
        content.remove(0);
        return new PlayerInfo(numberOfPlayers, playerNames);
    }

}



