package me.ht9.end.feature.module.client.clickgui;

import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;

@Description(value = "The graphical user interface for the client.")
public final class ClickGui extends Module
{
    private static final ClickGui instance = new ClickGui();

    private ClickGui()
    {
    }

//    @Override
//    public void onUpdate(UpdateEvent event) {
//        event.setPitch(-90.0f);
//    }

    public static ClickGui getInstance()
    {
        return instance;
    }
}
