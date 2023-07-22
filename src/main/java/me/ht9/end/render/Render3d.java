package me.ht9.end.render;

import me.ht9.end.shader.GlStateManager;
import me.ht9.end.util.Globals;
import net.minecraft.src.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class Render3d implements Globals {
    public static void drawShaderBox(final AxisAlignedBB axis) {
        render3d(() -> {
            AxisAlignedBB newBB = AxisAlignedBB.method_87(
                    axis.minX,
                    axis.minY,
                    axis.minZ,
                    axis.maxX,
                    axis.maxY,
                    axis.maxZ
            );
            renderFilledBox(newBB.minX, newBB.minY, newBB.minZ, newBB.maxX, newBB.maxY, newBB.maxZ);
        });
    }

    private static void renderFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glEnd();
    }

    public static void setupRender3d() {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_HINT_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            System.err.println("OpenGL error during setupRender3d: " + error);
        }
    }

    public static void endRender3d() {
        GL11.glPopAttrib();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            System.err.println("OpenGL error during endRender3d: " + error);
        }
    }


    public static void render3d(Runnable renderCode) {
        GlStateManager.pushMatrix();

        setupRender3d();

        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();

        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        boolean texture2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GlStateManager.disableTexture2D();

        boolean cull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        GlStateManager.disableCull();

        boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GlStateManager.disableAlpha();

        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        renderCode.run();

        GlStateManager.shadeModel(GL11.GL_FLAT);

        endRender3d();

        if (alpha) {
            GlStateManager.enableAlpha();
        }

        if (cull) {
            GlStateManager.enableCull();
        }

        if (texture2d) {
            GlStateManager.enableTexture2D();
        }

        if (!blend) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        if (lighting) {
            GlStateManager.enableLighting();
        }

        GlStateManager.popMatrix();
    }
}
