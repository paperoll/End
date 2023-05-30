package me.ht9.end.util;

import me.ht9.end.End;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.shader.Framebuffer;
import me.ht9.end.shader.GlStateManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author bon
 */

public final class RenderUtils implements Globals
{
    public static Framebuffer framebuffer;
    //private static final CFontRenderer fontRenderer_icons;

    static
    {
        try
        {
//            Font icons = Font.createFont(Font.TRUETYPE_FONT, End.class.getResourceAsStream("/assets/end/fonts/icons.ttf"));
//            fontRenderer_icons = new CFontRenderer(icons.deriveFont(30.0F));
        } catch (Throwable t)
        {
            End.getLogger().error("Failed to load fonts.", t);
            throw new RuntimeException(t);
        }
    }

    public static float getRenderPartialTicks()
    {
        return ((IMinecraft) mc).getTimer().renderPartialTicks;
    }

//    public static void drawIconString(String s, float x, float y)
//    {
//        fontRenderer_icons.drawStringWithShadow(s, x, y, Color.WHITE.getRGB());
//    }

    public static void setupRender2d()
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }

    public static void endRender2d()
    {
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRect(float x, float y, float width, float height, Color color)
    {
        setupRender2d();

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(GL11.GL_QUADS);
        tessellator.setColorRGBA_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        tessellator.addVertex(x, y, 0.0F);
        tessellator.setColorRGBA_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        tessellator.addVertex(x, y + height, 0.0F);
        tessellator.setColorRGBA_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        tessellator.addVertex(x + width, y + height, 0.0F);
        tessellator.setColorRGBA_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        tessellator.addVertex(x + width, y, 0.0F);
        tessellator.draw();

        endRender2d();
    }

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

//        ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
//        GlStateManager.bindTexture(framebuffer.texture);
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
//        tessellator.addVertexWithUV(0.0D, sr.getScaledHeight(), 0.0D, 0.0D, 0.0D);
//        tessellator.addVertexWithUV(sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1.0D, 0.0D);
//        tessellator.addVertexWithUV(sr.getScaledWidth(), 0.0D, 0.0D, 1.0D, 1.0D);
//        tessellator.draw();
    }

    public static void setupRender3d()
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }

    public static void endRender3d()
    {
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
