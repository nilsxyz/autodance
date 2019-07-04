package net.motionrupf.autodance.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MathUtil {
    public static float findRequiredYaw(BlockPos pos, EntityPlayerSP player) {
        double differenceX = pos.getX() - player.posX + 0.5;
        double differenceZ = pos.getZ() - player.posZ + 0.5;

        return (player.rotationYaw + MathHelper.wrapDegrees(((float)(MathHelper.atan2(differenceZ, differenceX)
                * (180D / Math.PI)) - 90.0F) - player.rotationYaw));
    }

    public static float findRequiredPitch(BlockPos pos, EntityPlayerSP player) {
        double differenceX = pos.getX() - player.posX + 0.5;
        double differenceY = pos.getY() - (player.posY + player.eyeHeight);
        double differenceZ = pos.getZ() - player.posZ + 0.5;

        double squareRoot = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ);

        return MathHelper.wrapDegrees(
                (float)(-(MathHelper.atan2(differenceY, squareRoot) * (180D / Math.PI)))
        );
    }

    public static double diff(double a, double b) {
        if(a >= b) {
            return Math.abs(a - b);
        } else {
            return Math.abs(b - a);
        }
    }
}
