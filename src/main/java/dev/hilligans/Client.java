package dev.hilligans;

import dev.hilligans.GameInstance;
import dev.hilligans.game.Game;
import dev.hilligans.network.ClientNetwork;

public class Client {

    public GameInstance gameInstance;
    public ClientNetwork network;
    public Game game = new Game();

    public int playerID = 0;


    public Client(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public String getUsername() {
        return "";
    }



}
