package me.ht9.end.core;

import me.ht9.end.event.factory.EventFactory;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.client.arraylist.ArrayList;
import me.ht9.end.feature.module.client.hud.HUD;
import me.ht9.end.feature.module.combat.aura.Aura;
import me.ht9.end.feature.module.combat.velocity.Velocity;
import me.ht9.end.feature.module.exploit.crasher.Crasher;
import me.ht9.end.feature.module.exploit.instantmine.InstantMine;
import me.ht9.end.feature.module.exploit.nofall.NoFall;
import me.ht9.end.feature.module.misc.antiaim.AntiAim;
import me.ht9.end.feature.module.misc.freecam.Freecam;
import me.ht9.end.feature.module.misc.norotate.NoRotate;
import me.ht9.end.feature.module.movement.pvpspeed.PVPSpeed;
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
        Velocity.getInstance().enable();
        ArrayList.getInstance().enable();
        Aura.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_R, Bind.BindType.KEYBOARD);
        InstantMine.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_I, Bind.BindType.KEYBOARD);
        Freecam.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_G, Bind.BindType.KEYBOARD);
        HUD.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_U, Bind.BindType.KEYBOARD);
        AntiAim.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_N, Bind.BindType.KEYBOARD);
        Crasher.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_C, Bind.BindType.KEYBOARD);
        Speed.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_Z, Bind.BindType.KEYBOARD);
        PVPSpeed.getInstance().getToggleBind().getValue().setKey(Keyboard.KEY_V, Bind.BindType.KEYBOARD);
        //FileUtils.loadModules(FileUtils.MODULES_FILE);
        //FileUtils.loadClickGUI();
    }
}