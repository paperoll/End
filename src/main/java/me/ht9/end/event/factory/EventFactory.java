package me.ht9.end.event.factory;

import me.ht9.end.End;
import me.ht9.end.core.Registries;
import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.InputEvent;
import me.ht9.end.event.events.RenderGameOverlayEvent;
import me.ht9.end.event.events.RenderWorldPassEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.util.Globals;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Comparator;

public final class EventFactory implements Globals
{
    private static final EventFactory instance = new EventFactory();

    public void init()
    {
        Listeners listeners = new Listeners();
        End.bus().register(listeners);
    }

    private static final class Listeners
    {
        public float rotationRenderPitch;
        private float prevRotationRenderPitch;

//        @EventHandler
//        public void onFMLNetworkEvent$ClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
//        {
//            Registries.getInstance().getModules().forEach(module ->
//            {
//                if (module.isEnabled())
//                {
//                    module.onLogIn();
//                }
//            });
//        }

        @SubscribeEvent
        public void onUpdate(UpdateEvent event)
        {
            Registries.getInstance().getModules().sort(Comparator.comparingInt(m -> ((Module) m).getUpdatePriority()));
            Registries.getInstance().getModules().forEach(module ->
            {
                if (module.isEnabled())
                {
                    module.onUpdate(event);
                }
            });

            if (event.getStage() == UpdateEvent.Stage.PRE)
            {
                mc.thePlayer.rotationYaw = event.getYaw();
                mc.thePlayer.renderYawOffset = event.getYaw();
                this.rotationRenderPitch = event.getPitch();
            }
        }

        @SubscribeEvent
        public void onRenderGameOverlayEvent$Text(RenderGameOverlayEvent.Text event)
        {
            Registries.getInstance().getModules().forEach(module ->
            {
                if (module.isEnabled())
                {
                    module.onRender2d(event.getPartialTicks());
                }
            });
        }

        @SubscribeEvent
        public void onRenderWorldPass(RenderWorldPassEvent event)
        {
            Registries.getInstance().getModules().forEach(module ->
            {
                if (module.isEnabled())
                {
                    module.onRender3d(event.getPartialTicks());
                }
            });
        }

//        @SubscribeEvent
//        public void FMLNetworkEvent$ClientDisconnectionFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
//        {
//            Registries.getInstance().getModules().forEach(module ->
//            {
//                if (module.isEnabled())
//                {
//                    module.onLogOut();
//                }
//            });
//        }

//        @SubscribeEvent
//        public void onRenderEntity(RenderEntityEvent event)
//        {
//            if (event.getEntity().equals(mc.thePlayer) && mc.getRenderManager().playerViewY != 180.0F)
//            {
//                event.setHeadPitch(this.prevRotationRenderPitch + ((this.rotationRenderPitch - this.prevRotationRenderPitch) * RenderUtils.getRenderPartialTicks()));
//                this.prevRotationRenderPitch = event.getHeadPitch();
//            }
//        }

//        @SubscribeEvent
//        public void onInputEvent$KeyInput(InputEvent.KeyInputEvent event)
//        {
//            if (Keyboard.getEventKey() == Keyboard.KEY_NONE)
//            {
//                return;
//            }
//
//            if (Keyboard.isRepeatEvent())
//            {
//                return;
//            }
//
//            Registries.getInstance().getModules().forEach(m ->
//            {
//                if (m.isEnabled())
//                {
//                    for (Setting<?> setting : m.getSettings())
//                    {
//                        if (setting.getValue() instanceof Bind && !setting.equals(m.getToggleBind()))
//                        {
//                            @SuppressWarnings("unchecked")
//                            Bind bind = ((Setting<Bind>) setting).getValue();
//                            if (bind.getType() == Bind.BindType.KEYBOARD && bind.getKey() == Keyboard.getEventKey())
//                            {
//                                bind.runAction();
//                            }
//                        }
//                    }
//                }
//
//                Bind bind = m.getToggleBind().getValue();
//                if (bind.getType() == Bind.BindType.KEYBOARD && bind.getKey() == Keyboard.getEventKey())
//                {
//                    Setting<Module.BindMode> mode = m.getBindMode();
//                    switch (mode.getValue())
//                    {
//                        case Normal:
//                            if (Keyboard.getEventKeyState())
//                            {
//                                bind.runAction();
//                            }
//                            break;
//                        case Hold:
//                            bind.runAction();
//                            break;
//                    }
//                }
//            });
//        }
//
//        @SubscribeEvent
//        public void onInputEvent$MouseInput(InputEvent.MouseInputEvent event)
//        {
//            Registries.getInstance().getModules().forEach(m ->
//            {
//                if (m.isEnabled())
//                {
//                    for (Setting<?> setting : m.getSettings())
//                    {
//                        if (setting.getValue() instanceof Bind && !setting.equals(m.getToggleBind()))
//                        {
//                            @SuppressWarnings("unchecked")
//                            Bind bind = ((Setting<Bind>) setting).getValue();
//                            if (bind.getType() == Bind.BindType.MOUSE && bind.getKey() == Mouse.getEventButton())
//                            {
//                                bind.runAction();
//                            }
//                        }
//                    }
//                }
//
//                Bind bind = m.getToggleBind().getValue();
//                if (bind.getType() == Bind.BindType.MOUSE && bind.getKey() == Mouse.getEventButton())
//                {
//                    Setting<Module.BindMode> mode = m.getBindMode();
//                    switch (mode.getValue())
//                    {
//                        case Normal:
//                            if (Mouse.getEventButtonState())
//                            {
//                                bind.runAction();
//                            }
//                            break;
//                        case Hold:
//                            bind.runAction();
//                            break;
//                    }
//                }
//            });
//        }

        @SubscribeEvent
        public void onInputEvent$KeyInput(InputEvent.KeyInputEvent event)
        {
            if (Keyboard.getEventKeyState())
            {
                Registries.getInstance().getBinds().forEach(bind ->
                {
                    if (bind.getParent() == null)
                    {
                        if (bind.getType() == Bind.BindType.KEYBOARD && bind.getKey() == Keyboard.getEventKey() && Keyboard.getEventKey() != Keyboard.KEY_NONE)
                        {
                            bind.runAction();
                        }
                    } else
                    {
                        Module parent = bind.getParent();
                        if (parent.isEnabled())
                        {
                            if (bind.getType() == Bind.BindType.KEYBOARD && bind.getKey() == Keyboard.getEventKey() && Keyboard.getEventKey() != Keyboard.KEY_NONE)
                            {
                                bind.runAction();
                            }
                        }
                    }
                });
            }
        }

        @SubscribeEvent
        public void onInputEvent$MouseInput(InputEvent.MouseInputEvent event) {
            if (Mouse.getEventButtonState())
            {
                Registries.getInstance().getBinds().forEach(bind ->
                {
                    if (bind.getParent() == null)
                    {
                        if (bind.getType() == Bind.BindType.MOUSE && bind.getKey() == Mouse.getEventButton())
                        {
                            bind.runAction();
                        }
                    } else
                    {
                        Module parent = bind.getParent();
                        if (parent.isEnabled())
                        {
                            if (bind.getType() == Bind.BindType.MOUSE && bind.getKey() == Mouse.getEventButton())
                            {
                                bind.runAction();
                            }
                        }
                    }
                });
            }
        }
    }

    public static EventFactory getInstance()
    {
        return instance;
    }
}
