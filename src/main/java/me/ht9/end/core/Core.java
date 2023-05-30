package me.ht9.end.core;

import me.ht9.end.event.factory.EventFactory;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.client.hud.HUD;
import me.ht9.end.feature.module.misc.antiaim.AntiAim;
import me.ht9.end.feature.module.render.esp.ESP;
import me.ht9.end.feature.module.render.truesight.TrueSight;
import me.ht9.end.shader.Framebuffer;
import me.ht9.end.shader.renderer.OpenGlHelper;
import me.ht9.end.util.Globals;
import me.ht9.end.util.RenderUtils;
import me.ht9.end.util.RotationUtils;
import org.lwjgl.input.Keyboard;

public final class Core implements Globals
{
    public static void init()
    {
        EventFactory.getInstance().init();
        Registries.getInstance().init();
        RotationUtils.Manager.init();
        ESP.getInstance().enable();
        TrueSight.getInstance().enable();
        HUD.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_G, Bind.BindType.KEYBOARD);
        AntiAim.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_V, Bind.BindType.KEYBOARD);
        //FileUtils.loadModules(FileUtils.MODULES_FILE);
        //FileUtils.loadClickGUI();
    }
}