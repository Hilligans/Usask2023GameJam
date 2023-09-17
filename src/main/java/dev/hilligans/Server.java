package dev.hilligans;

import dev.hilligans.game.Game;
import dev.hilligans.network.ClientNetwork;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.ServerNetwork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    public GameInstance gameInstance;
    public ServerNetwork serverNetwork;
    public Game game = new Game();

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public boolean running = true;

    public void startThreadLoop() {
        executorService.scheduleAtFixedRate(() -> {
            try {
                if(running) {
                    game.tickEntities();
                    int state = game.checkState();
                    if(state != 0) {
                        running = false;




                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 15, TimeUnit.MILLISECONDS);
    }


    public Server(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void sendPacket(PacketBase packetBase) {
        game.sendPacketToAll(packetBase);
    }
}
