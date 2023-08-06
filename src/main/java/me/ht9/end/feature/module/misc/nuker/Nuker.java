package me.ht9.end.feature.module.misc.nuker;

import com.google.common.base.Supplier;
import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PlayerDamageBlockEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.util.NetworkUtils;
import me.ht9.end.util.Timer;
import net.minecraft.src.Block;
import net.minecraft.src.Packet14BlockDig;

import java.util.ArrayList;
import java.util.Objects;

@Description(value = "Automatically jurgens blocks.")
public class Nuker extends Module
{
    private static final Nuker instance = new Nuker();

    private final Setting<Integer> nukerRadius = new Setting<>("Radius", 2, 5, 6);
    private final Setting<Boolean> excludeBlocksBelow = new Setting<>("ExcludeBlocksBelow", true);
    private final Setting<Boolean> yOnEnable = new Setting<>("YOnEnable", true, this.excludeBlocksBelow::getValue);

    private final ArrayList<Block> whitelistedBlocks = new ArrayList<>();
    private final ArrayList<Integer[]> positions = new ArrayList<>();
    private Timer timer = new Timer();;
    private int playerY;

    private Nuker()
    {
        this.setArrayListInfo(() -> String.valueOf(whitelistedBlocks.size()));
    }

    @SubscribeEvent
    public void onBlockClicked(PlayerDamageBlockEvent event)
    {
        int blockId = mc.theWorld.getBlockId(event.getX(), event.getY(), event.getZ());
        Block block = Block.blocksList[blockId];

        if (!this.whitelistedBlocks.contains(block))
        {
            this.whitelistedBlocks.add(block);
            mc.ingameGUI.addChatMessage("Added '" + block.getBlockName() + "' to the whitelist.");
        } else
        {
            mc.ingameGUI.addChatMessage("'" + block.getBlockName() + "' is already in the whitelist.");
        }
    }

    @Override
    public void onEnable()
    {
        if (this.yOnEnable.getValue() && this.excludeBlocksBelow.getValue())
        {
            this.playerY = (int) Math.floor(mc.thePlayer.posY);
        }
        this.timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event)
    {
        boolean shouldSelect = !mc.thePlayer.isDead;
        positions.clear();
        int range = this.nukerRadius.getValue();
        int playerX = (int) Math.floor(mc.thePlayer.posX);
        if (!this.yOnEnable.getValue())
        {
            this.playerY = (int) Math.floor(mc.thePlayer.posY);
        }
        int playerZ = (int) Math.floor(mc.thePlayer.posZ);

        for (int x = playerX - range; x <= playerX + range; x++)
        {
            for (int y = playerY - range; y <= playerY + range; y++)
            {
                for (int z = playerZ - range; z <= playerZ + range; z++)
                {
                    double distanceSq = (x - playerX) * (x - playerX) + (y - playerY) * (y - playerY) + (z - playerZ) * (z - playerZ);
                    if (distanceSq <= range * range)
                    {
                        if (this.excludeBlocksBelow.getValue() && (y < (playerY - 1)))
                        {
                            continue;
                        }

                        int blockId = mc.theWorld.getBlockId(x, y, z);
                        Block block = Block.blocksList[blockId];

                        if (shouldSelect)
                        {
                            for (Block blocks : whitelistedBlocks)
                            {
                                if (block != null && Objects.equals(blocks, block))
                                {
                                    if (mc.thePlayer.ticksExisted % 2 == 0)
                                    {
                                        positions.add(new Integer[]{x, y, z});
                                        for(int i = 0; i < 15; i++)
                                        {
                                            mc.method_2145().addToSendQueue(new Packet14BlockDig(0, x, y, z, 1));
                                            mc.method_2145().addToSendQueue(new Packet14BlockDig(1, x, y, z, 1));
                                            mc.method_2145().addToSendQueue(new Packet14BlockDig(1, x, y, z, 1));
                                            mc.method_2145().addToSendQueue(new Packet14BlockDig(1, x, y, z, 1));
                                            mc.method_2145().addToSendQueue(new Packet14BlockDig(1, x, y, z, 1));
                                            mc.method_2145().addToSendQueue(new Packet14BlockDig(1, x, y, z, 1));
                                        }
                                        mc.playerController.sendBlockRemoved(x, y, z, 1);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Nuker getInstance()
    {
        return instance;
    }
}
