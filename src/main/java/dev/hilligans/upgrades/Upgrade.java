package dev.hilligans.upgrades;

import dev.hilligans.GameInstance;
import dev.hilligans.client.graphics.*;
import dev.hilligans.game.Game;
import dev.hilligans.game.Player;
import dev.hilligans.registry.IRegistryElement;
import dev.hilligans.resource.ResourceLocation;

import java.util.ArrayList;

public class Upgrade implements IRegistryElement {

    public String upgradeName;

    public int upgradeID = 0;

    public String upgradeImageName;
    public GameInstance gameInstance;

    public Texture texture;

    public String customUpgradeDetail;


    public int damage;
    public int health;
    public float reloadSpeed = 1;
    public int reloadCount;
    public float speed = 1;
    public float temporarySpeedModifier = 1;

    public int tier;

    public Upgrade(String upgradeName, String upgradeImageName, int tier) {
        this.upgradeName = upgradeName;
        this.upgradeImageName = upgradeImageName;
        this.texture = new Texture("images/upgrades/" + upgradeImageName);
        this.tier = tier;
    }

    public Upgrade damage(int damage) {
        this.damage = damage;
        return this;
    }

    public Upgrade health(int health) {
        this.health = health;
        return this;
    }

    public Upgrade speed(float multiplier) {
        this.speed = multiplier;
        return this;
    }

    public Upgrade temporarySpeed(float temporarySpeedModifier) {
        this.temporarySpeedModifier = temporarySpeedModifier;
        return this;
    }

    public Upgrade reloadSpeed(float reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
        return this;
    }

    public Upgrade reloadCount(int reloadCount) {
        this.reloadCount = reloadCount;
        return this;
    }

    public Upgrade customUpgradeMessage(String message) {
        this.customUpgradeDetail = message;
        return this;
    }

    @Override
    public void load(GameInstance gameInstance) {
        IRegistryElement.super.load(gameInstance);
        this.gameInstance = gameInstance;
    }

    @Override
    public String getResourceName() {
        return null;
    }

    @Override
    public String getUniqueName() {
        return null;
    }

    public ArrayList<String> getLines() {
        ArrayList<String> lines = new ArrayList<>();
        if(damage != 0) {
            lines.add(damage + " damage");
        }
        if(health != 0) {
            lines.add("+" + health + " health");
        }
        if(speed != 1) {
            lines.add(speed + "x speed");
        }
        if(temporarySpeedModifier != 1) {
            lines.add(temporarySpeedModifier + "x speed on hit");
        }
        if(reloadSpeed != 1) {
            lines.add(reloadSpeed + "x reload speed");
        }
        if(reloadCount != 0) {
            lines.add(reloadCount + " added shots");
        }
        if(customUpgradeDetail != null) {
            lines.add(customUpgradeDetail);
        }
        return lines;
    }

    @Override
    public String toString() {
        return "Upgrade{" +
                "upgradeName='" + upgradeName + '\'' +
                ", upgradeID=" + upgradeID +
                ", upgradeImageName='" + upgradeImageName + '\'' +
                ", damage=" + damage +
                ", health=" + health +
                ", speed=" + speed +
                ", tier=" + tier +
                '}';
    }
}
