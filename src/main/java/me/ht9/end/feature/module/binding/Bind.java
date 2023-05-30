package me.ht9.end.feature.module.binding;

import me.ht9.end.core.Registries;
import me.ht9.end.feature.Feature;
import me.ht9.end.feature.module.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class Bind extends Feature
{
    private final Module parent;
    private BindType type;
    private int key;
    private Runnable action;

    public Bind(int key, BindType type, Module parent) {
        this.key = key;
        this.type = type;
        this.parent = parent;
        switch (type) {
            case KEYBOARD: {
                this.name = Keyboard.getKeyName(key);
                break;
            }
            case MOUSE: {
                this.name = Mouse.getButtonName(key);
                break;
            }
        }
        Registries.getInstance().getBinds().registerFeature(this);
    }

    public Bind withAction(Runnable action) {
        this.action = action;
        return this;
    }

    public void runAction() {
        action.run();
    }

    public int getKey() {
        return this.key;
    }

    public BindType getType() {
        return this.type;
    }

    public Module getParent() {
        return this.parent;
    }

    public void setKey(int key, BindType type) {
        this.key = key;
        this.type = type;
        switch (type) {
            case KEYBOARD: {
                this.name = Keyboard.getKeyName(key);
                break;
            }
            case MOUSE: {
                this.name = Mouse.getButtonName(key);
                break;
            }
        }
    }

    public enum BindType {
        KEYBOARD,
        MOUSE
    }
}