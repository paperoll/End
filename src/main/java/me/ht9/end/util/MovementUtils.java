package me.ht9.end.util;

public final class MovementUtils implements Globals
{
    public static double[] getDirectionalSpeed(float speed, float yaw)
    {
        float moveForward = mc.thePlayer.movementInput.moveForward;
        float moveStrafe = mc.thePlayer.movementInput.moveStrafe;

        if (moveForward != 0.0F)
        {
            if (moveStrafe >= 1.0F)
            {
                yaw += moveForward > 0.0F ? -45 : 45;
            } else if (moveStrafe <= -1.0F)
            {
                yaw += moveForward > 0.0F ? 45 : -45;
            }
            if (moveForward > 0.0F)
            {
                moveForward = 1.0F;
            } else if (moveForward < 0.0f)
            {
                moveForward = -1.0F;
            }
            moveStrafe = 0.0F;
        }

        double motionX = speed * MathHelper.sin((yaw + 90.0F) * 0.017453292F);
        double motionZ = speed * MathHelper.cos((yaw + 90.0F) * 0.017453292F);

        return new double[] { moveForward * motionZ + moveStrafe * motionX, moveForward * motionX - moveStrafe * motionZ };
    }

    public static double getAbsoluteSpeed(double motionX, double motionZ)
    {
        return Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionZ, 2));
    }
}
