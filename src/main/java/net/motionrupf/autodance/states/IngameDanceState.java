package net.motionrupf.autodance.states;

import net.minecraft.block.BlockStainedHardenedClay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.motionrupf.autodance.AutodanceMod;
import net.motionrupf.autodance.util.MathUtil;
import net.motionrupf.autodance.util.ScoreboardUtil;
import net.motionrupf.autodance.util.TextUtil;
import net.motionrupf.autodance.util.WorldUtil;

import java.util.Random;

public class IngameDanceState {
    private final AutodanceMod mod;
    private final Random random;

    private float oldPitch = Float.MAX_VALUE;
    private BlockPos randomPos;

    public IngameDanceState(AutodanceMod mod, Random random) {
        this.mod = mod;
        this.random = random;
    }

    public boolean shouldUpdate(TickEvent.PlayerTickEvent event) {
        return mod.isAutodanceEnabled() && event.player == Minecraft.getMinecraft().player &&
                ScoreboardUtil.testSidebarTitle(event.player.getWorldScoreboard(),
                        s -> TextUtil.getWithoutFormatting(s).equalsIgnoreCase("BlockParty"));
    }

    public void update(EntityPlayerSP player) {
        ItemStack stack = player.inventory.getStackInSlot(4);

        GameSettings settings = Minecraft.getMinecraft().gameSettings;

        int forwardBinding = settings.keyBindForward.getKeyCode();
        int sprintBinding = settings.keyBindSprint.getKeyCode();
        int attackBinding = settings.keyBindAttack.getKeyCode();
        int jumpBinding = settings.keyBindJump.getKeyCode();

        BlockPos fixedPlayerPos = WorldUtil.getNextSolidOrOriginalPosOffset(player, 2);

        if(stack.getItem() instanceof ItemBlock &&
                ((ItemBlock) stack.getItem()).getBlock() instanceof BlockStainedHardenedClay) {
            ItemBlock itemBlock = (ItemBlock) stack.getItem();
            BlockStainedHardenedClay block = (BlockStainedHardenedClay) itemBlock.getBlock();

            IBlockState state = block.getStateFromMeta(itemBlock.getMetadata(stack));
            mod.setAimedColor(state.getValue(BlockStainedHardenedClay.COLOR));

            BlockPos target = WorldUtil.findNext(player.getEntityWorld(), fixedPlayerPos, state);

            mod.setAimedPos(target);

            if(target != null) {
                if(!target.equals(fixedPlayerPos)) {
                    player.rotationYaw = MathUtil.findRequiredYaw(target, player);
                    KeyBinding.setKeyBindState(forwardBinding, true);
                    KeyBinding.setKeyBindState(sprintBinding, true);
                } else {
                    KeyBinding.setKeyBindState(forwardBinding, false);
                    KeyBinding.setKeyBindState(sprintBinding, false);
                }

                KeyBinding.setKeyBindState(jumpBinding, fixedPlayerPos.distanceSq(target) > 3);

                randomPos = null;
            }
        } else {
            mod.setAimedColor(null);
            mod.setAimedPos(null);

            BlockPos beaconPos = WorldUtil.findNext(player.getEntityWorld(), fixedPlayerPos.up(),
                    Blocks.BEACON.getDefaultState());
            if(beaconPos != null) {
                if(oldPitch == Float.MAX_VALUE) {
                    oldPitch = player.rotationPitch;
                }

                player.rotationYaw = MathUtil.findRequiredYaw(beaconPos, player);
                player.rotationPitch = MathUtil.findRequiredPitch(beaconPos, player);

                KeyBinding.setKeyBindState(forwardBinding, true);
                KeyBinding.setKeyBindState(sprintBinding, true);

                if(beaconPos.distanceSq(player.getPosition()) < 3) {
                    KeyBinding.setKeyBindState(attackBinding, true);
                } else {
                    KeyBinding.setKeyBindState(attackBinding, false);
                }

                return;
            }

            if(oldPitch != Float.MAX_VALUE) {
                player.rotationPitch = oldPitch;
                oldPitch = Float.MAX_VALUE;
            }

            KeyBinding.setKeyBindState(attackBinding, false);
            KeyBinding.setKeyBindState(jumpBinding, random.nextInt(5) == 0);

            if(randomPos == null || randomPos.equals(fixedPlayerPos)) {
                BlockPos newRandomPos = WorldUtil.findRandom(
                        player.getEntityWorld(), fixedPlayerPos, random, true);
                if(newRandomPos != null) {
                    randomPos = newRandomPos;
                }
            }

            if(randomPos != null) {
                float newYaw = MathUtil.findRequiredYaw(randomPos, player);
                double difference = MathUtil.diff(player.rotationYaw, newYaw);
                if(difference < 60) {
                    player.rotationYaw = newYaw;
                    KeyBinding.setKeyBindState(forwardBinding, true);
                    KeyBinding.setKeyBindState(sprintBinding, true);
                } else {
                    KeyBinding.setKeyBindState(forwardBinding, false);
                    KeyBinding.setKeyBindState(sprintBinding, false);
                    randomPos = null;
                }
            } else {
                KeyBinding.setKeyBindState(forwardBinding, false);
                KeyBinding.setKeyBindState(sprintBinding, false);
            }
        }

    }
}
