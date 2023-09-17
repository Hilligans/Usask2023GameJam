package dev.hilligans;

import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.Renderer;
import dev.hilligans.client.graphics.Window;
import dev.hilligans.client.graphics.screens.GameScreen;
import dev.hilligans.client.graphics.screens.UpgradeSelectScreen;
import dev.hilligans.client.graphics.screens.WaitingScreen;
import dev.hilligans.network.*;
import dev.hilligans.util.ArgumentContainer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class Main {

    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);

    public static final String PROJECT_NAME = "FoodFight";
    public static Main main;

    public Client client;
    public Server server;

    public Renderer renderer;
    public GlUtils glUtils;
    public GameInstance gameInstance = GameInstance.GAME_INSTANCE;
    public Window window;

    public static ArgumentContainer argumentContainer;

    public Main() {
        client = new Client(gameInstance);
        server = new Server(gameInstance);
    }

    public void run() {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = new Window(client, PROJECT_NAME, 1920 , 1000);

        GL.createCapabilities();

        glEnable(GL_DEBUG_OUTPUT);

        glDebugMessageCallback(new GLDebugMessageCallback() {
            @Override
            public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
                if(severity == GL43.GL_DEBUG_SEVERITY_NOTIFICATION) {
                    System.out.println(MemoryUtil.memUTF8(message));
                } else {
                    System.err.println(MemoryUtil.memUTF8(message));
                }
            }
        }, 0);

        glfwWindowHint(GLFW_SAMPLES, 4);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);
        glEnable(GL_PROGRAM_POINT_SIZE);
        glfwSwapInterval(1);

        glUtils = new GlUtils(gameInstance);
        renderer = new Renderer(glUtils, window);
        renderer.openScreen = new WaitingScreen();

        gameInstance.registerContent();
        gameInstance.load();
        gameInstance.loadGraphics(glUtils);
        glUtils.setupStringRenderer();

        renderer.renderLoop();
    }


    public static void main(String[] args) {
        argumentContainer = new ArgumentContainer(args);
        Protocols.setupProtocol();
        main = new Main();

        if(argumentContainer.getBoolean("-server", false)) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        main.server.startThreadLoop();
                        ServerNetwork serverNetwork = new ServerNetwork(Protocols.PROTOCOL);
                        main.server.serverNetwork = serverNetwork;
                        System.out.println("Starting Server");
                        serverNetwork.startServer("9995");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            thread.start();
        }

        if(argumentContainer.getBoolean("-client", false)) {
            main.client.network = new ClientNetwork(Protocols.PROTOCOL);
            Thread thread1 = new Thread(() -> {
                try {
                    Thread.sleep(1700);
                    main.client.network.joinServer("localhost", "9995", main.client);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            thread1.start();
        }
        main.run();
    }

    public static Client getClient() {
        return main.client;
    }

    public static Server getServer() {
        return main.server;
    }

    public static String getFormattedName(String name) {
        return PROJECT_NAME + ":" + name;
    }
}