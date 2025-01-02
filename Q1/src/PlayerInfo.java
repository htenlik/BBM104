public class PlayerInfo {
    /**
     * The number of players in the game.
     */
    private int numberOfPlayers;
    /**
     * Player names, separated by commas.
     */

    private String players;
    /**
     * Constructs a new PlayerInfo object.
     *
     * @param numberOfPlayers the number of players in the game
     * @param playerNames     the names of the players, separated by commas
     */

    public PlayerInfo(int numberOfPlayers, String playerNames) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = playerNames;
    }
    /**
     * Retrieves the number of players.
     *
     * @return the number of players
     */

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    /**
     * Retrieves the names of the players.
     *
     * @return the names of the players
     */
    public String getPlayers() {
        return players;
    }
}
