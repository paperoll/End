package me.ht9.end.feature;

import me.ht9.end.util.Globals;

public abstract class Feature implements Globals
{
    protected String name;
    protected String[] aliases;

    public final String getName()
    {
        return this.name;
    }

    public final String[] getAliases()
    {
        return this.aliases;
    }
}
