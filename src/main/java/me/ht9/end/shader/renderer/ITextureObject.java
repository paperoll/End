package me.ht9.end.shader.renderer;

import me.ht9.end.shader.resources.IResourceManager;

import java.io.IOException;

public interface ITextureObject
{
    void loadTexture(IResourceManager var1) throws IOException;

    int getGlTextureId();
}