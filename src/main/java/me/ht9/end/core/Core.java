package me.ht9.end.core;

import me.ht9.end.event.factory.EventFactory;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.client.hud.HUD;
import me.ht9.end.feature.module.exploit.crasher.Crasher;
import me.ht9.end.feature.module.exploit.nofall.NoFall;
import me.ht9.end.feature.module.misc.antiaim.AntiAim;
import me.ht9.end.feature.module.misc.norotate.NoRotate;
import me.ht9.end.feature.module.movement.speed.Speed;
import me.ht9.end.feature.module.render.esp.ESP;
import me.ht9.end.feature.module.render.truesight.TrueSight;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.shader.Framebuffer;
import me.ht9.end.shader.renderer.OpenGlHelper;
import me.ht9.end.util.Globals;
import me.ht9.end.util.RenderUtils;
import me.ht9.end.util.RotationUtils;
import net.minecraft.src.Session;
import org.lwjgl.input.Keyboard;

public final class  Core implements Globals
{
    public static void init()
    {
        EventFactory.getInstance().init();
        Registries.getInstance().init();
        RotationUtils.Manager.init();
        ESP.getInstance().enable();
        NoFall.getInstance().enable();
        NoRotate.getInstance().enable();
        TrueSight.getInstance().enable();
        HUD.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_G, Bind.BindType.KEYBOARD);
        AntiAim.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_V, Bind.BindType.KEYBOARD);
        Crasher.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_C, Bind.BindType.KEYBOARD);
        Speed.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_Z, Bind.BindType.KEYBOARD);
        //FileUtils.loadModules(FileUtils.MODULES_FILE);
        //FileUtils.loadClickGUI();
    }
}