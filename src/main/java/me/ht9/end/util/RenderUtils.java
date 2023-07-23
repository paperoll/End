package me.ht9.end.util;

import me.ht9.end.shader.Framebuffer;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.opengl.GL11;

public final class RenderUtils implements Globals
{
    public static Framebuffer framebuffer;

    public static void drawShader(Framebuffer framebuffer)
    {
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.texture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(0, 0);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(0, res.getScaledHeight());
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(res.getScaledWidth(), res.getScaledHeight());
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(res.getScaledWidth(), 0);
        GL11.glEnd();
    }
}
