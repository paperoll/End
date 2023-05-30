package me.ht9.end.feature.module.render.esp;

import com.google.common.io.CharStreams;
import me.ht9.end.End;
import me.ht9.end.shader.Framebuffer;
import me.ht9.end.util.Globals;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public final class OutlineShader implements Globals
{
    private static final OutlineShader instance = new OutlineShader();

    private final int programId;
    private final Map<String, Integer> uniformMap = new HashMap<>();
    private Framebuffer framebuffer;

    public OutlineShader()
    {
        try
        {
            int vertexId = this.loadVertex();
            InputStream is = this.getClass().getResourceAsStream("/assets/end/shaders/outline.frag");
            String src = this.readAndShut(is);
            int fragId = this.loadShader(src, GL20.GL_FRAGMENT_SHADER);
            this.programId = ARBShaderObjects.glCreateProgramObjectARB();
            ARBShaderObjects.glAttachObjectARB(this.programId, vertexId);
            ARBShaderObjects.glAttachObjectARB(this.programId, fragId);
            ARBShaderObjects.glLinkProgramARB(this.programId);
            ARBShaderObjects.glValidateProgramARB(this.programId);

            for (String uniform : new String[] { "resolution", "time", "red", "green", "blue", "fill", "rainbow" })
            {
                int address = GL20.glGetUniformLocation(this.programId, uniform);
                this.uniformMap.put(uniform, address);
            }
        } catch (Throwable t)
        {
            End.getLogger().error("Failed to load ESP outline shader: ", t);
            throw new RuntimeException(t);
        }
    }

    private int loadShader(String src, int mode)
    {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(mode);
        ARBShaderObjects.glShaderSourceARB(shader, src);
        ARBShaderObjects.glCompileShaderARB(shader);
        return shader;
    }

    private int loadVertex() throws IOException
    {
        InputStream is = this.getClass().getResourceAsStream("/assets/end/shaders/vertex.vert");
        String src = this.readAndShut(is);
        return this.loadShader(src, GL20.GL_VERTEX_SHADER);
    }

    @SuppressWarnings("all")
    private String readAndShut(InputStream is) throws IOException
    {
        String read = CharStreams.toString(new InputStreamReader(is, Charset.defaultCharset()));
        IOUtils.closeQuietly(is);
        return read;
    }

    public int getUniform(String name)
    {
        return this.uniformMap.get(name);
    }

    public Framebuffer getFramebuffer()
    {
        if (this.framebuffer != null)
        {
            this.framebuffer.delete();
        }
        this.framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        return this.framebuffer;
    }

    public int getProgramId()
    {
        return this.programId;
    }

    public static OutlineShader getInstance()
    {
        return instance;
    }
}
