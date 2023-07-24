package me.ht9.end.event.events;

import me.ht9.end.event.Event;
import net.minecraft.src.ScaledResolution;

import java.util.ArrayList;

public class RenderGameOverlayEvent extends Event
{
    public float getPartialTicks()
    {
        return partialTicks;
    }

    public ScaledResolution getResolution()
    {
        return resolution;
    }

    public ElementType getType()
    {
        return type;
    }

    public static enum ElementType
    {
        ALL,
        HELMET,
        PORTAL,
        CROSSHAIRS,
        ARMOR,
        HEALTH,
        AIR,
        HOTBAR,
        EXPERIENCE,
        TEXT,
        CHAT,
        DEBUG,
        FPS_GRAPH,
        VIGNETTE
    }

    private final float partialTicks;
    private final ScaledResolution resolution;
    private final ElementType type;

    public RenderGameOverlayEvent(float partialTicks, ScaledResolution resolution)
    {
        this.partialTicks = partialTicks;
        this.resolution = resolution;
        this.type = null;
    }

    private RenderGameOverlayEvent(RenderGameOverlayEvent parent, ElementType type)
    {
        this.partialTicks = parent.getPartialTicks();
        this.resolution = parent.getResolution();
        this.type = type;
    }

    public static class Pre extends RenderGameOverlayEvent
    {
        public Pre(RenderGameOverlayEvent parent, ElementType type)
        {
            super(parent, type);
        }
    }

    public static class Post extends RenderGameOverlayEvent
    {
        public Post(RenderGameOverlayEvent parent, ElementType type)
        {
            super(parent, type);
        }
    }

    public static class Text extends Pre
    {
        public Text(RenderGameOverlayEvent parent)
        {
            super(parent, ElementType.TEXT);
        }
    }

    public static class Chat extends Pre
    {
        private int posX;
        private int posY;

        public Chat(RenderGameOverlayEvent parent, int posX, int posY)
        {
            super(parent, ElementType.CHAT);
            this.setPosX(posX);
            this.setPosY(posY);
        }

        public int getPosX()
        {
            return posX;
        }

        public void setPosX(int posX)
        {
            this.posX = posX;
        }

        public int getPosY()
        {
            return posY;
        }

        public void setPosY(int posY)
        {
            this.posY = posY;
        }
    }
}