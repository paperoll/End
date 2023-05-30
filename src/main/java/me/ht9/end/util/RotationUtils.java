package me.ht9.end.util;

import me.ht9.end.End;
import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;

import java.util.concurrent.ThreadLocalRandom;

public final class RotationUtils implements Globals
{
    public final static class Manager
    {
        private static final Manager instance = new Manager();

        private float lastServerYaw;
        private float lastServerPitch;

        private Manager()
        {
            End.bus().register(this);
        }

        @SubscribeEvent
        public void onPacket(PacketEvent event)
        {
            if (event.getPacket() instanceof Packet13PlayerLookMove)
            {
                Packet13PlayerLookMove packet = (Packet13PlayerLookMove) event.getPacket();
                this.lastServerYaw = packet.yaw;
                this.lastServerPitch = packet.pitch;
            } else if (event.getPacket() instanceof Packet12PlayerLook)
            {
                Packet12PlayerLook packet = (Packet12PlayerLook) event.getPacket();
                this.lastServerYaw = packet.yaw;
                this.lastServerPitch = packet.pitch;
            }
        }

        public float[] easeTo(float[] rotations, float strength, float difference)
        {
            float yawChange = MathHelper.wrapAngleTo180_float(rotations[0] - this.lastServerYaw);
            float pitchChange = MathHelper.wrapAngleTo180_float(rotations[1] - this.lastServerPitch);

            float yawChangeFactor = Math.abs(yawChange) / 180.0F;
            float pitchChangeFactor = Math.abs(pitchChange) / 180.0F;

            float maxYawChange = (float) MathHelper.getRandomDoubleInRange(ThreadLocalRandom.current(), strength * yawChangeFactor, (strength + difference) * yawChangeFactor);
            float maxPitchChange = (float) MathHelper.getRandomDoubleInRange(ThreadLocalRandom.current(), strength * pitchChangeFactor, (strength + difference) * pitchChangeFactor);

            if (Math.abs(yawChange) >= maxYawChange)
            {
                if (yawChange > 0.0F)
                {
                    yawChange = maxYawChange;
                } else
                {
                    yawChange = -maxYawChange;
                }
            }

            if (Math.abs(pitchChange) >= maxPitchChange)
            {
                if (pitchChange > 0.0F)
                {
                    pitchChange = maxPitchChange;
                } else
                {
                    pitchChange = -maxPitchChange;
                }
            }

            return new float[] { this.lastServerYaw + yawChange, this.lastServerPitch + pitchChange };
        }

        public boolean areRotationsEqual(float[] first, float[] second)
        {
            float firstYaw = MathHelper.wrapAngleTo180_float(first[0]);
            float firstPitch = MathHelper.wrapAngleTo180_float(first[1]);
            float secondYaw = MathHelper.wrapAngleTo180_float(second[0]);
            float secondPitch = MathHelper.wrapAngleTo180_float(second[1]);
            return Math.abs(firstYaw - secondYaw) < 1.0F && Math.abs(firstPitch - secondPitch) < 1.0F;
        }

        public static void init()
        {
        }

        public float getLastServerYaw()
        {
            return this.lastServerYaw;
        }

        public float getLastServerPitch()
        {
            return this.lastServerPitch;
        }

        public static Manager getInstance()
        {
            return instance;
        }
    }
}
