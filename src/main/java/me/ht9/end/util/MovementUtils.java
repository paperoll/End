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

    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static void setSpeed(double moveSpeed) {
        setSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }
}
