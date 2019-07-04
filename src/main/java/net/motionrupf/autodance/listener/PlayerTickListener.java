package net.motionrupf.autodance.listener;

import net.minecraft.block.Block;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.motionrupf.autodance.AutodanceMod;
import org.apache.logging.log4j.Logger;

import java.security.Key;
import java.util.*;

public class PlayerTickListener {
    private final AutodanceMod mod;
    private final Logger logger;
    private final Random random;

    private BlockPos randomPos;

    public PlayerTickListener(AutodanceMod mod, Logger logger) {
        this.mod = mod;
        this.logger = logger;
        random = new Random();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.player == Minecraft.getMinecraft().player && mod.isEnabled()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ItemStack stack = player.inventory.getStackInSlot(4);
            GameSettings settings = Minecraft.getMinecraft().gameSettings;

            int forwardBinding = settings.keyBindForward.getKeyCode();
            int sprintBinding = settings.keyBindSprint.getKeyCode();

            if(stack.getItem() instanceof ItemBlock &&
                    ((ItemBlock) stack.getItem()).getBlock() instanceof BlockStainedHardenedClay) {
                ItemBlock itemBlock = (ItemBlock) stack.getItem();
                BlockStainedHardenedClay block = (BlockStainedHardenedClay) itemBlock.getBlock();

                IBlockState state = block.getStateFromMeta(itemBlock.getMetadata(stack));
                mod.setAimedColor(state.getValue(BlockStainedHardenedClay.COLOR));

                BlockPos target = findNext(player.getEntityWorld(), player.getPosition().down(), state);
                mod.setAimedPos(target);

                if(target != null) {
                    if(!target.equals(player.getPosition().down())) {
                        player.rotationYaw = findRequiredYaw(target, player);
                        KeyBinding.setKeyBindState(forwardBinding, true);
                        KeyBinding.setKeyBindState(sprintBinding, true);
                    } else {
                        KeyBinding.setKeyBindState(forwardBinding, false);
                        KeyBinding.setKeyBindState(sprintBinding, false);
                    }

                    randomPos = null;
                }
            } else {
                mod.setAimedColor(null);
                mod.setAimedPos(null);

                if(randomPos == null || randomPos.equals(player.getPosition())) {
                    BlockPos newRandomPos = findRandom(player.getEntityWorld(), player.getPosition().down());
                    if(newRandomPos != null) {
                        randomPos = newRandomPos;
                    }
                }

                if(randomPos != null) {
                    float newYaw = findRequiredYaw(randomPos, player);
                    double difference = diff(player.rotationYaw, newYaw);
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

    @SuppressWarnings("Duplicates")
    private BlockPos findNext(World world, BlockPos from, IBlockState blockState) {
        double currentDistance = Double.MAX_VALUE;

        if(world.getBlockState(from).equals(blockState)) {
            return from;
        }

        BlockPos bestPos = null;
        for(int xOffset = 0; xOffset < 200; xOffset++) {
            for(int yOffset = 0; yOffset < 200; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        for(int xOffset = 0; xOffset < 200; xOffset++) {
            for(int yOffset = 0; yOffset > -200; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        for(int xOffset = 0; xOffset > -200; xOffset--) {
            for(int yOffset = 0; yOffset < 200; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        for(int xOffset = 0; xOffset > -200; xOffset--) {
            for(int yOffset = 0; yOffset > -200; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        return bestPos;
    }

    @SuppressWarnings("Duplicates")
    private BlockPos findRandom(World world, BlockPos from) {
        List<BlockPos> positions = new ArrayList<>();

        for(int xOffset = 0; xOffset < 5; xOffset++) {
            for(int yOffset = 0; yOffset < 5; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR) {
                    positions.add(newPos);
                }
            }
        }

        for(int xOffset = 0; xOffset > -5; xOffset--) {
            for(int yOffset = 0; yOffset < 5; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR) {
                    positions.add(newPos);
                }
            }
        }

        for(int xOffset = 0; xOffset < 5; xOffset++) {
            for(int yOffset = 0; yOffset > -5; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR) {
                    positions.add(newPos);
                }
            }
        }

        for(int xOffset = 0; xOffset > -5; xOffset--) {
            for(int yOffset = 0; yOffset > -5; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR) {
                    positions.add(newPos);
                }
            }
        }

        if(positions.size() < 1) {
            return null;
        }

        int r = random.nextInt(positions.size() * 2);
        if(r > positions.size() - 1) {
            return null;
        } else {
            return positions.get(r);
        }
    }

    private float findRequiredYaw(BlockPos pos, EntityPlayerSP player) {
        double differenceX = pos.getX() - player.posX + 0.5;
        double differenceZ = pos.getZ() - player.posZ + 0.5;

        return (player.rotationYaw + MathHelper.wrapDegrees(((float)(MathHelper.atan2(differenceZ, differenceX)
            * (180D / Math.PI)) - 90.0F) - player.rotationYaw));
    }

    private double diff(double a, double b) {
        if(a >= b) {
            return Math.abs(a - b);
        } else {
            return Math.abs(b - a);
        }
    }
}
