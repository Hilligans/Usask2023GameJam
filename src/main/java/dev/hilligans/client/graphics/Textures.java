package dev.hilligans.client.graphics;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;

public class Textures {

    //aseprite -b sheet.ase --save-as {slice}.png

    public static final Texture ARROW = new Texture("images/arrow_clockwise.png");
    public static final Texture UPGRADE_BACKGROUND = new Texture("images/card_background.png");
    public static final Texture DIVIDER = new Texture("images/divider.png");
    public static final Texture TILE = new Texture("images/tile.png");
    public static final Texture WALL = new Texture("images/wall.png");
    public static final Texture GREEN = new Texture("images/green.png");
    public static final Texture RED = new Texture("images/red.png");
    public static final Texture NORTH_WALL = new Texture("images/north_wall.png");
    public static final Texture HORIZONTAL_TABLE = new Texture("images/horizontal_table.png");
    public static final Texture CHAIR = new Texture("images/chair.png");
    public static final Texture VENDING_MACHINE = new Texture("images/vending_machine.png");


    public static final Texture MALE_HEAD = new Texture("images/players/male_head.png");
    public static final Texture FEMALE_HEAD = new Texture("images/players/female_head.png");
    public static final Texture BLUE_BODY = new Texture("images/players/blue_body.png");
    public static final Texture RED_BODY = new Texture("images/players/red_body.png");
    public static final Texture RED_SHOT_FULL = new Texture("images/players/red_shot_full.png");
    public static final Texture RED_SHOT_EMPTY = new Texture("images/players/red_shot_empty.png");
    public static final Texture BLUE_SHOT_FULL = new Texture("images/players/blue_shot_full.png");
    public static final Texture BLUE_SHOT_EMPTY = new Texture("images/players/blue_shot_empty.png");


    public static final Texture[] NUMBERS = new Texture[10];

    public static void loadTextures(GameInstance gameInstance) {
        gameInstance.TEXTURES.put(Main.getFormattedName("arrow"), ARROW);
        gameInstance.TEXTURES.put(Main.getFormattedName("upgrade_background"), UPGRADE_BACKGROUND);
        gameInstance.TEXTURES.put(Main.getFormattedName("divider"), DIVIDER);
        gameInstance.TEXTURES.put(Main.getFormattedName("tile"), TILE);
        gameInstance.TEXTURES.put(f("male_head"), MALE_HEAD);
        gameInstance.TEXTURES.put(f("female_head"), FEMALE_HEAD);
        gameInstance.TEXTURES.put(f("blue_body"), BLUE_BODY);
        gameInstance.TEXTURES.put(f("red_body"), RED_BODY);

        gameInstance.TEXTURES.put(f("red_shot_full"), RED_SHOT_FULL);
        gameInstance.TEXTURES.put(f("red_shot_empty"), RED_SHOT_EMPTY);
        gameInstance.TEXTURES.put(f("blue_shot_full"), BLUE_SHOT_FULL);
        gameInstance.TEXTURES.put(f("blue_shot_empty"), BLUE_SHOT_EMPTY);

        gameInstance.TEXTURES.put(f("wall"), WALL);
        gameInstance.TEXTURES.put(f("red"), RED);
        gameInstance.TEXTURES.put(f("green"), GREEN);
        gameInstance.TEXTURES.put(f("north_wall"), NORTH_WALL);
        gameInstance.TEXTURES.put(f("horizontal_table"), HORIZONTAL_TABLE);
        gameInstance.TEXTURES.put(f("chair"), CHAIR);
        gameInstance.TEXTURES.put(f("vending_machine"), VENDING_MACHINE);

        for(int x = 0; x < NUMBERS.length; x++) {
            NUMBERS[x] = new Texture("images/numbers/" + x + ".png");
            gameInstance.TEXTURES.put(f(String.valueOf(x)), NUMBERS[x]);
        }
    }

    public static String f(String f) {
        return Main.getFormattedName(f);
    }
}
