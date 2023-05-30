package me.ht9.end.event.events;

import me.ht9.end.event.Event;

public final class UpdateEvent extends Event
{
    private final Stage stage;
    private double packetX;
    private double packetY;
    private double packetZ;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public UpdateEvent(double packetX, double packetY, double packetZ, float yaw, float pitch, boolean onGround, Stage stage) {
        this.packetX = packetX;
        this.packetY = packetY;
        this.packetZ = packetZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.stage = stage;
    }

    public double getPacketX() {
        return this.packetX;
    }

    public void setPacketX(double packetX) {
        this.packetX = packetX;
    }

    public double getPacketY() {
        return this.packetY;
    }

    public void setPacketY(double packetY) {
        this.packetY = packetY;
    }

    public double getPacketZ() {
        return this.packetZ;
    }

    public void setPacketZ(double packetZ) {
        this.packetZ = packetZ;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Stage getStage() {
        return this.stage;
    }

    public enum Stage {
        PRE,
        POST
    }
}