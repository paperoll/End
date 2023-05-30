package me.ht9.end.shader.resources;

import me.ht9.end.shader.resources.data.IMetadataSection;

import java.io.InputStream;

public interface IResource
{
    InputStream getInputStream();

    boolean hasMetadata();

    IMetadataSection getMetadata(String var1);
}